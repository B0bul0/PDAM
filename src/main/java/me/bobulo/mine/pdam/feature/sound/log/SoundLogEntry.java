package me.bobulo.mine.pdam.feature.sound.log;

import me.bobulo.mine.pdam.log.LogEntry;

import java.util.Objects;

public class SoundLogEntry implements LogEntry {

    private final long timestamp = System.currentTimeMillis();

    private final String soundName;
    private final float volume;
    private final float pitch;

    // Location data
    private final double x;
    private final double y;
    private final double z;

    public SoundLogEntry(String soundName, float volume, float pitch) {
        this.soundName = soundName;
        this.volume = volume;
        this.pitch = pitch;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public SoundLogEntry(String soundName, float volume, float pitch, double x, double y, double z) {
        this.soundName = soundName;
        this.volume = volume;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSoundName() {
        return soundName;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SoundLogEntry)) return false;
        SoundLogEntry that = (SoundLogEntry) o;
        return timestamp == that.timestamp
          && Float.compare(volume, that.volume) == 0
          && Float.compare(pitch, that.pitch) == 0
          && Double.compare(x, that.x) == 0
          && Double.compare(y, that.y) == 0
          && Double.compare(z, that.z) == 0
          && Objects.equals(soundName, that.soundName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, soundName, volume, pitch, x, y, z);
    }

    @Override
    public String toString() {
        return "SoundLogEntry{" +
          "timestamp=" + timestamp +
          ", soundName='" + soundName + '\'' +
          ", volume=" + volume +
          ", pitch=" + pitch +
          ", x=" + x +
          ", y=" + y +
          ", z=" + z +
          '}';
    }
}
