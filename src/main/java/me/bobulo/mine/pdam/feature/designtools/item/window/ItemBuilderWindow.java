package me.bobulo.mine.pdam.feature.designtools.item.window;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.designtools.item.Enchantment;
import me.bobulo.mine.pdam.feature.designtools.item.HideFlag;
import me.bobulo.mine.pdam.feature.designtools.item.ItemData;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.ItemColorUtil;
import me.bobulo.mine.pdam.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public class ItemBuilderWindow extends AbstractRenderItemWindow {

    private ItemData itemBuilder = new ItemData();

    private final ImString material = new ImString("", 256);

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
        itemBuilder = item;

        // Update UI values
        material.set(item.getMaterial());
        durability.set(item.getDurability());
        amount.set(item.getAmount());
        customName.set(item.getCustomName());
        customLore.set(item.getCustomLore());
        unbreakable.set(item.isUnbreakable());
        glowing.set(item.isGlowing());
        hideFlags.clear();
        hideFlags.addAll(item.getHideFlags());
        enchantments.clear();
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            enchantments.put(entry.getKey(), new ImInt(entry.getValue()));
        }
        colorRGB = ItemColorUtil.toRgb(item.getColor() == null ? 0 : item.getColor());
        skullValue.set(item.getSkullValue());
        skullName.set(item.getSkullName());
        repairCost.set(item.getRepairCost());
    }

    private void renderContent() {
        boolean disableImport = false;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.getHeldItem() == null) {
            disableImport = true;
        }

        if (disableImport) {
            beginDisabled();
        }

        if (button("Import from Hand") && !disableImport) {
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null) {
                importItem(ItemData.fromItemStack(heldItem));
            }
        }

        if (disableImport) {
            endDisabled();
        }

        if (beginChild("##EditSection", 0, 0, true)) {
            renderEditSection();
            endChild();
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
        if (inputText("##Item", material)) {
            itemBuilder.setMaterial(material.get());
        }

        text("Amount:");
        sameLine();
        setNextItemWidth(150);
        if (inputInt("##Amount", amount)) {
            itemBuilder.setAmount(amount.get());
            setMaxMinValue(amount, 1, 64);
        }

        sameLine();
        text("Durability:");
        sameLine();
        setNextItemWidth(150);
        if (inputInt("##Durability", durability)) {
            itemBuilder.setDurability(durability.get());
            setMaxMinValue(amount, 1, 10000);
        }

        separatorText("Display Properties:");

        text("Custom Name:");
        sameLine();
        if (inputText("##CustomName", customName)) {
            itemBuilder.setCustomName(customName.get());
        }

        text("Custom Lore:");
        if (inputTextMultiline("##Lore", customLore, -1, 0)) {
            itemBuilder.setCustomLore(customLore.get());
        }

        spacing();
        renderEnchantmentSection();

        if (treeNode("Other Properties")) {

            if (checkbox("Unbreakable", unbreakable)) {
                itemBuilder.setUnbreakable(unbreakable.get());
            }

            sameLine();

            if (checkbox("Glowing", glowing)) {
                itemBuilder.setGlowing(glowing.get());
            }

            sameLine();

            if (beginPopup("hideFlagsSelect")) {
                text("Hide Flags:");
                checkbox("Hide All", hideFlags.size() == HideFlag.VALUES.size());
                if (isItemClicked()) {
                    if (hideFlags.size() == HideFlag.VALUES.size()) {
                        hideFlags.clear();
                    } else {
                        hideFlags.addAll(HideFlag.VALUES);
                    }

                    itemBuilder.setHideFlags(hideFlags);
                }

                separator();
                for (HideFlag flag : HideFlag.VALUES) {
                    checkbox("Hide " + flag.name(), hideFlags.contains(flag));

                    if (isItemClicked()) {
                        if (hideFlags.contains(flag)) {
                            hideFlags.remove(flag);
                        } else {
                            hideFlags.add(flag);
                        }

                        itemBuilder.setHideFlags(hideFlags);
                    }
                }

                endPopup();
            }

            if (button("Hide Flags")) {
                openPopup("hideFlagsSelect");
            }

            if (inputInt("Repair Cost", repairCost)) {
                itemBuilder.setRepairCost(repairCost.get());
                setMaxMinValue(repairCost, 0, 10000);
            }

            renderSkullSection();

            if (colorEdit3("Color", colorRGB)) {
                itemBuilder.setColor(ItemColorUtil.toRgbInt(colorRGB));
            }
            treePop();
        }

        separator();

        if (disableGiving) {
            beginDisabled();
        }

        if (button("Give Item", 200, 20)) {
            ItemStack itemStack = itemBuilder.buildItem();
            if (itemStack == null) {
                return;
            }

            PlayerUtils.giveItem(itemStack);
        }

        if (disableGiving) {
            endDisabled();
        }

    }

    private void renderEnchantmentSection() {
        if (treeNode("Enchantment")) {

            enchantmentFilter.draw();

            for (Enchantment enchantment : Enchantment.VALUES) {
                if (!enchantmentFilter.passFilter(enchantment.getKey())) {
                    continue;
                }

                boolean enable = enchantments.containsKey(enchantment);
                checkbox(enchantment.getKey(), enable);

                if (isItemClicked()) {
                    if (enable) {
                        enchantments.remove(enchantment);
                        itemBuilder.getEnchantments().remove(enchantment);
                    } else {
                        enchantments.putIfAbsent(enchantment, new ImInt(1));
                        itemBuilder.getEnchantments().putIfAbsent(enchantment, 1);
                    }
                }

                if (!enchantments.containsKey(enchantment)) {
                    continue;
                }

                sameLine();

                setNextItemWidth(150);
                if (inputInt("##level" + enchantment.getKey(), enchantments.get(enchantment))) {
                    setMaxMinValue(enchantments.get(enchantment), 1, 100);
                    itemBuilder.getEnchantments().put(enchantment, enchantments.get(enchantment).intValue());
                }
            }

            treePop();
        }
    }

    private void renderSkullSection() {
        if (inputText("Skull Value", skullValue)) {
            itemBuilder.setSkullValue(skullValue.get());
        }

        if (isItemHovered()) {
            setTooltip("The Base64 value from the skin properties.");
        }

        if (inputText("Skull Name", skullName)) {
            itemBuilder.setSkullValue(skullName.get());
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
