package me.bobulo.mine.pdam.notification;

import java.awt.Color;

public enum NotificationLevel {

    INFO(new Color(200, 200, 200)),
    SUCCESS(new Color(70, 220, 90)),
    WARNING(new Color(255, 215, 80)),
    ERROR(new Color(230, 60, 60));

    private final int color;

    NotificationLevel(Color color) {
        this.color = color.getRGB();
    }

    public int getColor() {
        return color;
    }
}