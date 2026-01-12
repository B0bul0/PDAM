package me.bobulo.mine.pdam.log;

import me.bobulo.mine.pdam.util.FixedCircularHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class LogHistory<L extends LogEntry> {

    public static final int MAX_LOGS = 1_000_000;
    private static final int DEFAULT_LOGS_LIMIT = 500;

    private int maxLogLimit;
    private boolean paused = false;

    private FixedCircularHistory<L> logEntries;

    public LogHistory() {
        this(DEFAULT_LOGS_LIMIT);
    }

    public LogHistory(int maxLogLimit) {
        this.maxLogLimit = maxLogLimit;
        this.logEntries = new FixedCircularHistory<>(maxLogLimit);
    }

    public void addLogEntry(L entry) {
        if (entry == null || paused) {
            return;
        }

        logEntries.push(entry);
    }

    public L getLogEntry(int index) {
        return logEntries.get(index);
    }

    public void setMaxLogLimit(int newLimit) {
        if (newLimit == this.maxLogLimit) {
            return;
        }

        if (newLimit < 1) {
            newLimit = 1;
        } else if (newLimit > MAX_LOGS) {
            newLimit = MAX_LOGS;
        }

        this.maxLogLimit = newLimit;

        // Recreate with new capacity
        FixedCircularHistory<L> newList = new FixedCircularHistory<>(newLimit);
        List<L> snapshot = logEntries.snapshot();

        // Copy recent entries
        int start = Math.max(0, snapshot.size() - newLimit);
        for (int i = start; i < snapshot.size(); i++) {
            newList.push(snapshot.get(i));
        }

        this.logEntries = newList;
    }

    public void forEach(@NotNull Consumer<? super L> action) {
        logEntries.forEach(action);
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
