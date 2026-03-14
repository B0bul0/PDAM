package me.bobulo.mine.pdam.feature.packet;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A utility class to track the rate of packets over a specified time interval.
 * It maintains a count of packets received and calculates the rate based on the last snapshot.
 */
public final class PacketRateTracker {

    private final AtomicInteger currentCount = new AtomicInteger();
    private volatile int lastCount = 0;
    private final AtomicLong lastSnapshotNanos = new AtomicLong(System.nanoTime());

    private final long intervalNanos;

    public PacketRateTracker(int time, TimeUnit unit) {
        this.intervalNanos = unit.toNanos(time);
    }

    public void count() {
        currentCount.incrementAndGet();
    }

    private void rotateIfNeeded() {
        long now = System.nanoTime();
        long last = lastSnapshotNanos.get();
        if (now - last < intervalNanos) {
            return;
        }

        if (lastSnapshotNanos.compareAndSet(last, now)) {
            lastCount = currentCount.getAndSet(0);
        }
    }

    public int getCurrentRate() {
        rotateIfNeeded();
        return lastCount;
    }

}