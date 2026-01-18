package me.bobulo.mine.pdam.feature.item;

import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.item.ItemBuilder.HideFlag;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

import java.util.EnumSet;
import java.util.Set;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public class ItemBuilderWindow extends AbstractRenderItemWindow {

    private ItemBuilder itemBuilder = new ItemBuilder();

    private final ImString material = new ImString("minecraft:stone", 256);
    private final ImInt materialData = new ImInt(0);

    private final ImBoolean durability = new ImBoolean();

    private final ImString customName = new ImString("", 256);
    private final ImString customLore = new ImString("", 256);

    private final ImInt amount = new ImInt(1);

    private final ImBoolean unbreakable = new ImBoolean(false);

    private final Set<HideFlag> hideFlags = EnumSet.noneOf(HideFlag.class);

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

    private void renderContent() {
        if (inputText("Material", material)) {
            itemBuilder.setMaterial(material.get());
        }

        if (inputInt("Data", materialData)) {
            itemBuilder.setData(materialData.get());
        }

        if (checkbox("Durability", durability)) {
            itemBuilder.setDurability(durability.get());
        }

        if (inputInt("Amount", amount)) {
            itemBuilder.setAmount(amount.get());
            setMaxMinValue(amount, 1, 64);
        }

        if (inputText("Custom Name", customName)) {
            itemBuilder.setCustomName(customName.get());
        }

        if (inputTextMultiline("Custom Lore", customLore)) {
            itemBuilder.setCustomLore(customLore.get());
        }

        if (checkbox("Unbreakable", unbreakable)) {
            itemBuilder.setUnbreakable(unbreakable.get());
        }

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

        if (button("Build Item")) {
            ItemStack itemStack = itemBuilder.buildItem();
            Minecraft mc = Minecraft.getMinecraft();

            if (!mc.playerController.isInCreativeMode()) { // only works in creative
                return;
            }

            NetHandlerPlayClient netHandler = mc.getNetHandler();
            netHandler.addToSendQueue(new C10PacketCreativeInventoryAction(38, itemStack)); // 36 - 44 Hotbar
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
