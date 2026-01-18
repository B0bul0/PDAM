package me.bobulo.mine.pdam.feature.item;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.ItemColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public class ItemBuilderWindow extends AbstractRenderItemWindow {

    private ItemBuilder itemBuilder = new ItemBuilder();

    private final ImString material = new ImString("minecraft:stone", 256);

    private final ImInt durability = new ImInt();

    private final ImString customName = new ImString("", 256);
    private final ImString customLore = new ImString("", 256);

    private final ImInt amount = new ImInt(1);

    private final ImBoolean unbreakable = new ImBoolean(false);
    private final ImBoolean glowing = new ImBoolean(false);

    private final Set<HideFlag> hideFlags = EnumSet.noneOf(HideFlag.class);

    private final ImGuiTextFilter enchantmentFilter = new ImGuiTextFilter();
    private final Map<Enchantment, ImInt> enchantments = new HashMap<>();

    private float[] colorRGB = new float[]{0.0f, 0.0f, 0.0f};

    private final ImString skullValue = new ImString("", 512);

    public ItemBuilderWindow() {
        super("Item Builder");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(400, 450, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Item Builder##ItemBuilderWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void importItem(ItemBuilder item) {
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
        colorRGB = ItemColorUtil.toRgb(item.getColor());
        skullValue.set(item.getSkullValue());
    }

    private void renderContent() {
            if (button("Import from Hand")) {
                ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
                ItemBuilder itemBuilder1 = ItemBuilder.fromItemStack(itemStack);
                importItem(itemBuilder1);
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

        if (inputText("Item", material)) {
            itemBuilder.setMaterial(material.get());
        }

        if (inputInt("Durability", durability)) {
            itemBuilder.setDurability(durability.get());
        }

        if (inputInt("Amount", amount)) {
            itemBuilder.setAmount(amount.get());
            setMaxMinValue(amount, 1, 64);
        }

        separatorText("Display Properties:");

        if (inputText("Custom Name", customName)) {
            itemBuilder.setCustomName(customName.get());
        }

        if (inputTextMultiline("Custom Lore", customLore)) {
            itemBuilder.setCustomLore(customLore.get());
        }

        renderEnchantmentSection();

        separatorText("Other Properties:");

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
                }
            }

            endPopup();
        }

        if (button("Hide Flags")) {
            openPopup("hideFlagsSelect");
        }

        renderSkullSection();

        if (treeNode("Color")) {
            if (colorEdit3("Color", colorRGB)) {
                itemBuilder.setColor(ItemColorUtil.toRgbInt(colorRGB));
            }
            treePop();
        }

        if (disableGiving) {
            beginDisabled();
        }

        separator();

        setNextItemWidth(200);
        if (button("Build Item")) {
            ItemStack itemStack = itemBuilder.buildItem();
            Minecraft mc = Minecraft.getMinecraft();

            if (!mc.playerController.isInCreativeMode()) { // only works in creative
                return;
            }

            NetHandlerPlayClient netHandler = mc.getNetHandler();
            netHandler.addToSendQueue(new C10PacketCreativeInventoryAction(38, itemStack)); // 36 - 44 Hotbar
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
                    } else {
                        enchantments.putIfAbsent(enchantment, new ImInt(1));
                    }
                }

                if (!enchantments.containsKey(enchantment)) {
                    continue;
                }

                sameLine();

                setNextItemWidth(150);
                if (inputInt("##level" + enchantment.getKey(), enchantments.get(enchantment))) {
                    setMaxMinValue(enchantments.get(enchantment), 1, 100);
                }
            }
            treePop();
        }
    }

    private void renderSkullSection() {
        if (treeNode("Skull")) {
            if (inputText("Skull Value", skullValue)) {
                itemBuilder.setSkullValue(skullValue.get());
            }

            treePop();
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
