package me.bobulo.mine.pdam.ui.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotificationManager {

    private static final NotificationManager INSTANCE = new NotificationManager();

    private final List<Notification> notifications = new CopyOnWriteArrayList<>(); // Thread-safe list

    private NotificationManager() {
    }

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    public static void showInfo(String message) {
        show(message, NotificationLevel.INFO);
    }

    public static void showSuccess(String message) {
        show(message, NotificationLevel.SUCCESS);
    }

    public static void showWarning(String message) {
        show(message, NotificationLevel.WARNING);
    }

    public static void showError(String message) {
        show(message, NotificationLevel.ERROR);
    }

    public static void show(String message, NotificationLevel level) {
        INSTANCE.addNotification(new Notification(message, 3000L, level));
    }

    private void addNotification(Notification notification) {
        notifications.add(0, notification); // Top-down order
    }

    void removedExpiredNotifications() {
        notifications.removeIf(Notification::isExpired);
    }

    List<Notification> getNotifications() {
        return notifications;
    }

}