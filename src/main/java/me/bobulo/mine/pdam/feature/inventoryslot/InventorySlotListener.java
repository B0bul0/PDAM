package me.bobulo.mine.pdam.feature.inventoryslot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventorySlotListener {

    @SubscribeEvent
    public void onDrawBackground(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!InventorySlotInspector.ENABLED.get()) {
            return;
        }

        render(event.gui);
    }

    private void render(GuiScreen gui) {
        if (gui instanceof GuiContainer) {

            if (gui instanceof GuiContainerCreative) { // Skip creative mode
                return;
            }

            GuiContainer container = (GuiContainer) gui;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

            boolean onlyBackground = InventorySlotInspector.OVERLAY_PRIORITY.get() == 0;

            int guiLeft = ObfuscationReflectionHelper.getPrivateValue(
              GuiContainer.class,
              container,
              "guiLeft",
              "field_3394"
            );

            int guiTop = ObfuscationReflectionHelper.getPrivateValue(
              GuiContainer.class,
              container,
              "guiTop",
              "field_3395"
            );

            if (!onlyBackground) {
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
            }

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();


            for (Slot slot : container.inventorySlots.inventorySlots) {
                int x = slot.xDisplayPosition + guiLeft;
                int y = slot.yDisplayPosition + guiTop;

                String text = String.valueOf(slot.getSlotIndex());

                int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);

                fontRenderer.drawString(
                  text,
                  x + 8.5F - (textWidth / 2F),
                  y + 4F,
                  InventorySlotInspector.COLOR.get(), false
                );
            }

            GlStateManager.enableLighting();

            if (!onlyBackground) {
                GlStateManager.disableBlend();
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }

        }
    }

}
