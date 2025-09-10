package me.bobulo.mine.devmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MenuListener {

    private static final int DEVMOD_BUTTON_ID = 41114;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu) {
            event.buttonList.add(new GuiButton(
              DEVMOD_BUTTON_ID,
              5,
              5,
              100,
              20,
              "DevMod"
            ));
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu && event.button.id == DEVMOD_BUTTON_ID) {
            Minecraft.getMinecraft().displayGuiScreen(new FeaturesMenuGui(event.gui));
        }
    }

}
