package me.bobulo.mine.pdam.notification;

public final class Notifier {

    private Notifier() {
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
        NotificationManager.getInstance().addNotification(new Notification(message, 3000L, level));
    }

}