package me.bobulo.mine.pdam.imgui.util;

import imgui.flag.ImGuiCol;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;

/**
 * A simple notification drawer using ImGui tooltips.
 */
public class ImGuiNotificationDrawer {

    private final List<Notification> notifications = new ArrayList<>();

    public void draw() {
        if (notifications.isEmpty()) {
            return;
        }

        notifications.removeIf(notification -> {
            notification.timeRemaining--;
            return notification.timeRemaining <= 0;
        });

        if (notifications.isEmpty()) {
            return;
        }

        beginTooltip();
        pushStyleColor(ImGuiCol.Text, 1.0f, 0.0f, 0.0f, 1.0f);

        for (Notification notification : notifications) {
            text(notification.message);
        }

        popStyleColor();
        endTooltip();
    }

    public void addNotification(String message) {
        addNotification(message, 5 * 20); // Default duration 5 seconds (assuming 20 ticks per second)
    }

    public void addNotification(String message, int durationTicks) {
        Notification notification = new Notification();
        notification.message = message;
        notification.timeRemaining = durationTicks;
        notifications.add(notification);
    }

    private static class Notification {
        private String message;
        private int timeRemaining; // in ticks
    }

}
