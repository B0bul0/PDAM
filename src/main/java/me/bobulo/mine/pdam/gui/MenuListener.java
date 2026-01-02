package me.bobulo.mine.pdam.gui;

import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.gui.PacketLogGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MenuListener {

    private static final int PDAM_BUTTON_ID = 41114;
    private static final int PACKET_BUTTON_ID = 41115;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        // ESC menu
        if (event.gui instanceof GuiIngameMenu) {
            event.buttonList.add(new GuiButton(
              PDAM_BUTTON_ID,
              5,
              5,
              100,
              20,
              "PDAM"
            ));
        }

        if (event.gui instanceof GuiIngameMenu) {
            event.buttonList.add(new GuiButton(
              PACKET_BUTTON_ID,
              5 + 105,
              5,
              100,
              20,
              "Packet Data"
            ));
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu && event.button.id == PDAM_BUTTON_ID) {
            Minecraft.getMinecraft().displayGuiScreen(new FeaturesMenuGui(event.gui));
        } else if (event.gui instanceof GuiIngameMenu && event.button.id == PACKET_BUTTON_ID) {
            Minecraft.getMinecraft().displayGuiScreen(new PacketLogGuiScreen(PacketMonitorFeatureComponent.INSTANCE.getPacketEntries()));
        }
    }

}
