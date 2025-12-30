package me.bobulo.mine.pdam.ui.notification;

public class Notification {

    private final String message;
    private final long creationTime;
    private final long duration;
    private final NotificationLevel level;

    public Notification(String message, long duration, NotificationLevel level) {
        this.message = message;
        this.duration = duration;
        this.level = level;
        this.creationTime = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getDuration() {
        return duration;
    }

    public NotificationLevel getLevel() {
        return level;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > creationTime + duration;
    }
}