package me.bobulo.mine.pdam.notification;

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

    void addNotification(Notification notification) {
        notifications.add(0, notification); // Top-down order
    }

    void removedExpiredNotifications() {
        notifications.removeIf(Notification::isExpired);
    }

    List<Notification> getNotifications() {
        return notifications;
    }

}