package me.bobulo.mine.pdam.feature.inventoryslot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;

public class InventorySlotRender {

    public static void render(GuiScreen gui) {
        InventorySlotInspector context = InventorySlotInspector.context();
        if (!context.getFeature().isEnabled()) {
            return;
        }

        if (gui instanceof GuiContainer) {

            if (gui instanceof GuiContainerCreative) { // Skip creative mode
                return;
            }

            GuiContainer container = (GuiContainer) gui;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

            boolean onlyBackground = context.getOverlayPriorityConfig().get() == 0;

            if (!onlyBackground) {
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
            }

            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableLighting();

            for (Slot slot : container.inventorySlots.inventorySlots) {
                int x = slot.xDisplayPosition;
                int y = slot.yDisplayPosition;

                String text = String.valueOf(slot.getSlotIndex());

                int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);

                fontRenderer.drawString(
                  text,
                  x + 8.5F - (textWidth / 2F),
                  y + 4F,
                  context.getColorConfig().get(), false
                );
            }

            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();

            if (!onlyBackground) {
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }

        }
    }

}
