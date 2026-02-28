package me.bobulo.mine.pdam.imgui.util;

import imgui.flag.ImGuiCol;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;

/**
 * A simple notification drawer using ImGui tooltips.
 */
public class ImGuiNotificationDrawer {

    private final List<Notification> notifications = new ArrayList<>();

    /**
     * Draws the notifications as an ImGui tooltip.
     */
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

        for (Notification notification : notifications) {

            // Set text color based on notification type
            switch (notification.type) {
                case INFO: {
                    pushStyleColor(ImGuiCol.Text, 0.0f, 1.0f, 0.0f, 1.0f);
                    break;
                }
                case WARNING: {
                    pushStyleColor(ImGuiCol.Text, 1.0f, 1.0f, 0.0f, 1.0f);
                    break;
                }
                case ERROR: {
                    pushStyleColor(ImGuiCol.Text, 1.0f, 0.0f, 0.0f, 1.0f);
                    break;
                }
                default: {
                    pushStyleColor(ImGuiCol.Text, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
            }

            text(notification.message);
            popStyleColor();
        }

        endTooltip();
    }

    /* Convenience methods for different notification types */

    public void info(String message) {
        addNotification(message, NotificationType.INFO);
    }

    public void warning(String message) {
        addNotification(message, NotificationType.WARNING);
    }

    public void error(String message) {
        addNotification(message, NotificationType.ERROR);
    }

    /* Core method to add a notification */

    public void addNotification(String message, @NotNull NotificationType type) {
        addNotification(message, type, 5 * 20); // Default duration 5 seconds (assuming 20 ticks per second)
    }

    public void addNotification(String message, @NotNull NotificationType type, int durationTicks) {
        if (StringUtils.isEmpty(message) || containsNotification(message) != null) {
            return; // Avoid duplicate notifications
        }

        Notification notification = new Notification();
        notification.message = message;
        notification.type = type;
        notification.timeRemaining = durationTicks;
        notifications.add(notification);
    }

    private String containsNotification(String message) {
        for (Notification notification : notifications) {
            if (notification.message.equals(message)) {
                return notification.message;
            }
        }
        return null;
    }

    private static class Notification {
        private String message;
        private NotificationType type;
        private int timeRemaining; // in ticks
    }

    public enum NotificationType {
        INFO,
        WARNING,
        ERROR
    }

}
