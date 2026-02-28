package me.bobulo.mine.pdam.notification;

import me.bobulo.mine.pdam.ui.DisplayElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public final class NotificationDisplayElement implements DisplayElement {

    private static final NotificationManager notificationManager = NotificationManager.getInstance();

    @Override
    public void render(RenderGameOverlayEvent.Post event) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int yOffset = 0;

        // Remove expired notifications
        notificationManager.removedExpiredNotifications();

        for (Notification notification : notificationManager.getNotifications()) {
            int width = fontRenderer.getStringWidth(notification.getMessage()) + 20;
            int height = 20;
            int x = sr.getScaledWidth() - width - 5;
            int y = 5 + yOffset;

            // Background
            Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 120).getRGB());

            // Colored side bar
            Gui.drawRect(x, y, x + 4, y + height, notification.getLevel().getColor());

            // Text
            fontRenderer.drawStringWithShadow(notification.getMessage(),
              x + 10F,
              y + (height / 2f) - (fontRenderer.FONT_HEIGHT / 2f),
              Color.WHITE.getRGB());

            yOffset += height + 5;
        }
    }

}