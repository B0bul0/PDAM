package me.bobulo.mine.pdam.log;

import me.bobulo.mine.pdam.util.BoundedConcurrentList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class LogHistory<L extends LogEntry> {

    private static final int MAX_LOGS = 1_000_000;
    private static final int DEFAULT_LOGS_LIMIT = 500;

    private int maxLogLimit;
    private boolean paused = false;

    private BoundedConcurrentList<L> logEntries;

    public LogHistory() {
        this(DEFAULT_LOGS_LIMIT);
    }

    public LogHistory(int maxLogLimit) {
        this.maxLogLimit = maxLogLimit;
        this.logEntries = new BoundedConcurrentList<>(maxLogLimit);
    }

    public void addLogEntry(L entry) {
        if (entry == null || paused) {
            return;
        }

        logEntries.add(entry);
    }

    public void setMaxLogLimit(int newLimit) {
        if (newLimit < 1) {
            newLimit = 1;
        } else if (newLimit > MAX_LOGS) {
            newLimit = MAX_LOGS;
        }

        this.maxLogLimit = newLimit;

        // Recreate with new capacity
        BoundedConcurrentList<L> newList = new BoundedConcurrentList<>(newLimit);
        List<L> snapshot = logEntries.snapshot();

        // Copy recent entries
        int start = Math.max(0, snapshot.size() - newLimit);
        for (int i = start; i < snapshot.size(); i++) {
            newList.add(snapshot.get(i));
        }

        this.logEntries = newList;
    }

    @NotNull
    public List<L> getLogEntries() {
        return Collections.unmodifiableList(logEntries);
    }

    public int getMaxLogLimit() {
        return maxLogLimit;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public int size() {
        return logEntries.size();
    }

    public void clear() {
        logEntries.clear();
    }

}
