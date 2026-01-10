package me.bobulo.mine.pdam.imgui.window;

import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;
import me.bobulo.mine.pdam.log.LogEntry;
import me.bobulo.mine.pdam.log.LogHistory;

import static imgui.ImGui.*;

public class LogWindow<E extends LogEntry> implements ImGuiRenderable {

    private final LogHistory<E> logHistory;
    private final Formatter<E> formatter;

    private final ImString searchField = new ImString(256);

    private boolean copyToClipboard = false;
    private boolean autoScroll = true; // todo

    private final ImInt maxLogs;

    public LogWindow(LogHistory<E> logHistory) {
        this(logHistory, LogEntry::toString);
    }

    public LogWindow(LogHistory<E> logHistory, Formatter<E> formatter) {
        this.logHistory = logHistory;
        this.formatter = formatter;
        this.maxLogs = new ImInt(logHistory.getMaxLogLimit());
    }

    @Override
    public void newFrame() {
        renderContent();
    }

    private void renderContent() {

        if (button("Clear")) {
            clearLogs();
        }

        sameLine();
        if (button("Copy")) {
            copyToClipboard = true;
        }

        sameLine();
        String pauseResumeText = logHistory.isPaused() ? "Resume" : "Pause";
        if (button(pauseResumeText)) {
            logHistory.setPaused(!logHistory.isPaused());
        }

        sameLine();

        setNextItemWidth(200.0f);
        if (inputInt("Max Logs", maxLogs, 1, 100)) {
            logHistory.setMaxLogLimit(maxLogs.get());
        }

        separator();

        text("Total Logs: " + logHistory.size());

        separator();

        text("Search:");
        sameLine();
        pushItemWidth(300);

        inputText("##search", searchField, ImGuiInputTextFlags.None);

        popItemWidth();

        separator();

        renderLogsTable();
    }

    private void renderLogsTable() {

        float footerHeightToReserve = getStyle().getItemSpacing().y + getFrameHeightWithSpacing();

        if (beginChild("ScrollingRegion", 0, -footerHeightToReserve, false, ImGuiWindowFlags.HorizontalScrollbar)) {

            pushStyleVar(ImGuiStyleVar.ItemSpacing, 4f, 1f); // Tighten spacing

            if (copyToClipboard) {
                logToClipboard();
            }

            String filterText = searchField.get().trim().toLowerCase();

            for (E logEntry : logHistory.getLogEntries()) {
                String item = formatter.format(logEntry);
                if (!filterText.isEmpty() && !filterText.contains(item)) {
                    continue;
                }

                textUnformatted(item);
            }

            if (copyToClipboard) {
                logFinish();
                copyToClipboard = false;
            }

            if (autoScroll && getScrollY() >= getScrollMaxY()) {
                setScrollHereY(1.0f);
            }

            popStyleVar();
        }
        endChild();
    }

    private void clearLogs() {
        logHistory.clear();
    }

    @FunctionalInterface
    public interface Formatter<E> {
        String format(E entry);
    }

}
