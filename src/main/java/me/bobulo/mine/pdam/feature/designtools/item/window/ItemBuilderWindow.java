package me.bobulo.mine.pdam.feature.designtools.item.window;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.designtools.item.Enchantment;
import me.bobulo.mine.pdam.feature.designtools.item.HideFlag;
import me.bobulo.mine.pdam.feature.designtools.item.ItemData;
import me.bobulo.mine.pdam.feature.designtools.item.ItemDataFactory;
import me.bobulo.mine.pdam.imgui.util.ImGuiNotificationDrawer;
import me.bobulo.mine.pdam.imgui.util.TextSuggestionInput;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.ClipboardUtils;
import me.bobulo.mine.pdam.util.ColorUtil;
import me.bobulo.mine.pdam.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;

/**
 * A GUI window for building and customizing Minecraft items.
 */
public class ItemBuilderWindow extends AbstractRenderItemWindow {

    private final ImGuiNotificationDrawer notificationDrawer = new ImGuiNotificationDrawer();

    private ItemData itemData = new ItemData();

    private final TextSuggestionInput material = new TextSuggestionInput("##Item");

    private final ImInt durability = new ImInt();

    private final ImString customName = new ImString("", 256);
    private final ImString customLore = new ImString("", 256);

    private final ImInt amount = new ImInt(1);

    private final ImBoolean unbreakable = new ImBoolean(false);
    private final ImBoolean glowing = new ImBoolean(false);
    private final ImInt repairCost = new ImInt(0);

    private final Set<HideFlag> hideFlags = EnumSet.noneOf(HideFlag.class);

    private final ImGuiTextFilter enchantmentFilter = new ImGuiTextFilter();
    private final Map<Enchantment, ImInt> enchantments = new HashMap<>();

    private float[] colorRGB = new float[]{0.0f, 0.0f, 0.0f};

    private final ImString skullValue = new ImString("", 512);
    private final ImString skullName = new ImString("", 512);

    private final ImString jsonBuffer = new ImString("", 1024);

    private static final String[] ITEM_SUGGESTIONS = ItemNames.getItemNames().toArray(new String[0]);

    public ItemBuilderWindow() {
        super("Item Builder");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(500, 500, ImGuiCond.Once);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Item Builder##ItemBuilderWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void importItem(ItemData item) {
        itemData = item;

        // Update UI values
        material.setSelected(StringUtils.defaultString(item.getMaterial()));
        durability.set(item.getDurability());
        amount.set(item.getAmount());
        customName.set(StringUtils.defaultString(item.getCustomName()));
        customLore.set(StringUtils.defaultString(item.getCustomLore()));
        unbreakable.set(item.isUnbreakable());
        glowing.set(item.isGlowing());
        hideFlags.clear();
        hideFlags.addAll(item.getHideFlags());
        enchantments.clear();
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            enchantments.put(entry.getKey(), new ImInt(entry.getValue()));
        }
        colorRGB = ColorUtil.toRgb(item.getColor() == null ? 0 : item.getColor());
        skullValue.set(StringUtils.defaultString(item.getSkullValue()));
        skullName.set(StringUtils.defaultString(item.getSkullName()));
        repairCost.set(item.getRepairCost());
    }

    private void renderContent() {
        notificationDrawer.draw();

        renderImportSection();

        if (beginChild("##EditSection", 0, 0, true)) {
            renderEditSection();
        }
        endChild();
    }

    private void renderImportSection() {
        boolean disableHandImport = false;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.getHeldItem() == null) {
            disableHandImport = true;
        }

        if (disableHandImport) {
            beginDisabled();
        }

        if (button("Import from Hand") && !disableHandImport) {
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null) {
                importItem(ItemDataFactory.fromItemStack(heldItem));
            }
        }

        if (disableHandImport) {
            endDisabled();
        }

        if (beginPopup("ImportJson")) {
            if (button("From Clipboard")) {
                String clipboard = ClipboardUtils.getFromClipboard();
                if (StringUtils.isBlank(clipboard)) {
                    notificationDrawer.error("Clipboard is empty.");
                } else if (importItemFromJson(clipboard)) {
                    closeCurrentPopup(); // Close popup on successful import
                }
            }

            separator();

            text("Paste Json Below:");
            inputTextMultiline("##JsonImportText", jsonBuffer);
            if (button("Import")) {
                if (StringUtils.isBlank(jsonBuffer.get())) {
                    notificationDrawer.error("JSON input is empty.");
                } else if (importItemFromJson(jsonBuffer.get())) {
                    jsonBuffer.set("");
                    closeCurrentPopup(); // Close popup on successful import
                }
            }

            endPopup();
        }

        sameLine();
        if (button("Import from Json")) {
            openPopup("ImportJson");
        }
    }

    private boolean importItemFromJson(String json) {
        try {
            importItem(ItemDataFactory.fromJson(json));
            return true;
        } catch (Exception ignored) {
            notificationDrawer.error("Failed to import item from JSON.");
            return false;
        }
    }

    private void renderEditSection() {
        separatorText("Item Properties");

        boolean disableGiving = false;
        PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;
        if (playerController == null) {
            textColored(1.0f, 0.0f, 0.0f, 1.0f,
              "You need to be in a world to use the item builder.");
            disableGiving = true;
        } else {
            if (!playerController.isInCreativeMode()) {
                textColored(1.0f, 0.0f, 0.0f, 1.0f,
                  "You need to be in creative mode to use the item builder.");
                disableGiving = true;
            }
        }

        text("Item:");
        sameLine();
        if (material.draw(ITEM_SUGGESTIONS)) {
            itemData.setMaterial(material.getSelected());
        }

        text("Amount:");
        sameLine();
        setNextItemWidth(150);
        if (inputInt("##Amount", amount)) {
            itemData.setAmount(amount.get());
            setMaxMinValue(amount, 1, 64);
        }

        sameLine();
        text("Durability:");
        sameLine();
        setNextItemWidth(150);
        if (inputInt("##Durability", durability)) {
            itemData.setDurability(durability.get());
            setMaxMinValue(durability, 1, 10000);
        }

        separatorText("Display Properties:");

        text("Custom Name:");
        sameLine();
        if (inputText("##CustomName", customName)) {
            itemData.setCustomName(customName.get());
        }

        text("Custom Lore:");
        if (inputTextMultiline("##Lore", customLore, -1, 0)) {
            itemData.setCustomLore(customLore.get());
        }

        spacing();
        renderEnchantmentSection();
        spacing();

        if (treeNode("Other Properties")) {

            if (checkbox("Unbreakable", unbreakable)) {
                itemData.setUnbreakable(unbreakable.get());
            }

            sameLine();

            if (checkbox("Glowing", glowing)) {
                itemData.setGlowing(glowing.get());
            }

            sameLine();

            if (beginPopup("hideFlagsSelect")) {
                text("Hide Flags:");
                if (checkbox("Hide All", hideFlags.size() == HideFlag.VALUES.size())) {
                    if (hideFlags.size() == HideFlag.VALUES.size()) {
                        hideFlags.clear();
                    } else {
                        hideFlags.addAll(HideFlag.VALUES);
                    }

                    itemData.setHideFlags(hideFlags);
                }

                separator();
                for (HideFlag flag : HideFlag.VALUES) {
                    if (checkbox("Hide " + flag.name(), hideFlags.contains(flag))) {
                        if (hideFlags.contains(flag)) {
                            hideFlags.remove(flag);
                        } else {
                            hideFlags.add(flag);
                        }

                        itemData.setHideFlags(hideFlags);
                    }
                }

                endPopup();
            }

            if (button("Hide Flags")) {
                openPopup("hideFlagsSelect");
            }

            if (inputInt("Repair Cost", repairCost)) {
                itemData.setRepairCost(repairCost.get());
                setMaxMinValue(repairCost, 0, 10000);
            }

            renderSkullSection();

            if (colorEdit3("Color", colorRGB)) {
                itemData.setColor(ColorUtil.toRgbInt(colorRGB));
            }
            treePop();
        }

        spacing();
        separator();
        spacing();

        if (disableGiving) {
            beginDisabled();
        }

        if (button("Give Item", 150, 20)) {
            ItemStack itemStack = itemData.buildItem();
            if (itemStack == null) {
                notificationDrawer.error("Failed to build item. Check the material.");
                return;
            }

            PlayerUtils.giveToFirstFreeSlot(itemStack);
        }

        if (isItemHovered()) {
            setTooltip("Gives the item to the first empty slot in your inventory.");
        }

        sameLine();

        if (button("Add to Hand", 150, 20)) {
            ItemStack itemStack = itemData.buildItem();
            if (itemStack == null) {
                notificationDrawer.error("Failed to build item. Check the material.");
                return;
            }

            PlayerUtils.setHeldItem(itemStack);
        }

        if (isItemHovered()) {
            setTooltip("Adds the item directly to your hand.");
        }

        if (disableGiving) {
            endDisabled();
        }

        sameLine();

        if (button("To Json", 150, 20)) {
            String json = ItemDataFactory.toJson(itemData);
            ClipboardUtils.copyToClipboard(json);
            notificationDrawer.info("Item JSON copied to clipboard.");
        }

        if (isItemHovered()) {
            setTooltip("Copies the item's JSON representation to the clipboard.");
        }

    }

    private void renderEnchantmentSection() {
        if (beginPopup("EnchantmentsSettings")) {
            enchantmentFilter.draw();

            for (Enchantment enchantment : Enchantment.VALUES) {
                if (!enchantmentFilter.passFilter(enchantment.getKey())) {
                    continue;
                }

                boolean enable = enchantments.containsKey(enchantment);
                if (checkbox(enchantment.getKey(), enable)) {
                    if (enable) {
                        enchantments.remove(enchantment);
                        itemData.getEnchantments().remove(enchantment);
                    } else {
                        enchantments.putIfAbsent(enchantment, new ImInt(1));
                        itemData.getEnchantments().putIfAbsent(enchantment, 1);
                    }
                }

                if (!enchantments.containsKey(enchantment)) {
                    continue;
                }

                sameLine();

                setNextItemWidth(150);
                if (inputInt("##level" + enchantment.getKey(), enchantments.get(enchantment))) {
                    setMaxMinValue(enchantments.get(enchantment), 1, 100);
                    itemData.getEnchantments().put(enchantment, enchantments.get(enchantment).intValue());
                }
            }

            endPopup();
        }

        if (button("Enchantments", 200, 0)) {
            openPopup("EnchantmentsSettings");
        }
    }

    private void renderSkullSection() {
        if (inputText("Skull Value", skullValue)) {
            itemData.setSkullValue(skullValue.get());
        }

        if (isItemHovered()) {
            setTooltip("The Base64 value from the skin properties.");
        }

        if (inputText("Skull Name", skullName)) {
            itemData.setSkullName(skullName.get());
        }
    }

    private void setMaxMinValue(ImInt imInt, int min, int max) {
        if (imInt.get() < min) {
            imInt.set(min);
        } else if (imInt.get() > max) {
            imInt.set(max);
        }
    }

}
