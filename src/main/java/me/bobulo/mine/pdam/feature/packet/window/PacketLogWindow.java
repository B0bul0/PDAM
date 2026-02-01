package me.bobulo.mine.pdam.feature.packet.window;

import imgui.ImGuiListClipper;
import imgui.flag.*;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.log.LogHistory;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;
import static me.bobulo.mine.pdam.util.LocaleUtils.translateToLocal;

public final class PacketLogWindow extends AbstractRenderItemWindow {

    private final LogHistory<DisplayPacketLogEntry> logHistory;
    private final ImGuiListClipper logClipper = new ImGuiListClipper();
    private final ImInt maxLogs;

    private final ImBoolean filterServer = new ImBoolean(true);
    private final ImBoolean filterClient = new ImBoolean(true);

    private final ImString searchField = new ImString(256);
    private int filteredLogCount = 0;

    public PacketLogWindow(LogHistory<DisplayPacketLogEntry> logHistory) {
        super("Packet Log");
        this.logHistory = logHistory;
        this.maxLogs = new ImInt(logHistory.getMaxLogLimit());
    }

    @Override
    public void renderGui() {

        // Set initial window size and position
        setNextWindowSize(1000, 600, ImGuiCond.FirstUseEver);
        setNextWindowPos(100, 100, ImGuiCond.FirstUseEver);

        if (begin("Packet Log Viewer##PacketLogWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        text("Total Logs: " + logHistory.size());

        sameLine();
        text(" | ");
        sameLine();

        text("Filtered Logs: " + filteredLogCount);
        filteredLogCount = 0;

        if (button("Clear")) {
            clearLogs();
        }

        sameLine();
        String pauseResumeText = logHistory.isPaused()
          ? "Resume"
          : "Pause";
        if (button(pauseResumeText)) {
            togglePause();
        }

        sameLine();
        setNextItemWidth(160.0f);
        inputInt("Max Logs", maxLogs, 1, 100);
        if (isItemDeactivatedAfterEdit()) {
            logHistory.setMaxLogLimit(maxLogs.get());
        }

        separator();

        text("Search:");
        sameLine();

        pushItemWidth(300);
        inputText("##search", searchField, ImGuiInputTextFlags.None);
        popItemWidth();

        sameLine();
        if (beginPopupContextItem("packetFilterPopup")) {
            text("Direction Filter:");
            checkbox("Server Packets", filterServer);
            sameLine();
            checkbox("Client Packers", filterClient);

            endPopup();
        }

        if (button("Filter")) {
            openPopup("packetFilterPopup");
        }

        renderLogsTable();
    }

    private void renderLogsTable() {
        int flags = ImGuiTableFlags.Borders
          | ImGuiTableFlags.RowBg
          | ImGuiTableFlags.ScrollY
          | ImGuiTableFlags.Resizable
          | ImGuiTableFlags.Hideable
          | ImGuiTableFlags.Reorderable;

        if (beginTable("PacketLogsTable", 4, flags)) {
            tableSetupColumn("Time", ImGuiTableColumnFlags.WidthFixed, 85F);
            tableSetupColumn("Direcional", ImGuiTableColumnFlags.WidthFixed, 46F);
            tableSetupColumn("Packet Name", ImGuiTableColumnFlags.WidthFixed, 200F);
            tableSetupColumn("Packet Data", ImGuiTableColumnFlags.WidthStretch);
            tableHeadersRow();

            String searchText = searchField.get().toLowerCase().trim();
            List<DisplayPacketLogEntry> filteredLogs;

            boolean noDirectionFilter = filterServer.get() && filterClient.get();

            if (searchText.isEmpty() && noDirectionFilter) {
                filteredLogs = new ArrayList<>(logHistory.size());
                logHistory.forEach(filteredLogs::add);
            } else {
                filteredLogs = new ArrayList<>(logHistory.size() / 4);
                logHistory.forEach(entry -> {
                    if (!filterServer.get() && entry.getDirection() == PacketDirection.SERVER) {
                        return;
                    }

                    if (!filterClient.get() && entry.getDirection() == PacketDirection.CLIENT) {
                        return;
                    }

                    if (!searchText.isEmpty()) {
                        boolean matches = false;
                        if (entry.getSearchTerms() != null) {
                            for (String searchTerm : entry.getSearchTerms()) {
                                if (searchTerm.contains(searchText)) {
                                    matches = true;
                                    break;
                                }
                            }
                        }

                        if (!matches) {
                            return;
                        }
                    }

                    filteredLogs.add(entry);
                    filteredLogCount++;
                });
            }

            logClipper.begin(filteredLogs.size());
            while (logClipper.step()) {
                for (int i = logClipper.getDisplayStart(); i < logClipper.getDisplayEnd(); i++) {
                    if (i >= filteredLogs.size()) break;
                    DisplayPacketLogEntry entry = filteredLogs.get(i);

                    tableNextRow();

                    // Time column
                    tableNextColumn();
                    if (selectable(entry.getFormattedTime() + "##time" + i, false,
                      ImGuiSelectableFlags.SpanAllColumns | ImGuiSelectableFlags.AllowItemOverlap, 0, 0)) {
                        new PacketInfoWindow(entry).open();
                    }

                    if (beginPopupContextItem("log_popup##" + i)) {
                        text("Packet " + entry.getPacketName());
                        separator();

                        if (button("Copy Packet Data")) {
                            setClipboardText(entry.getPacketData());
                            closeCurrentPopup();
                        }

                        endPopup();
                    }

                    if (isItemHovered()) {
                        setTooltip("Packet Data: \n" + entry.getPacketData());
                    }

                    tableNextColumn();
                    if (entry.getDirection() == PacketDirection.SERVER) {
                        pushStyleColor(ImGuiCol.Text, 0.5f, 1.0f, 0.5f, 1.0f); // Green
                        text("SERVER");
                    } else {
                        pushStyleColor(ImGuiCol.Text, 1.0f, 0.5f, 0.5f, 1.0f); // Red
                        text("CLIENT");
                    }
                    popStyleColor();

                    tableNextColumn();
                    textUnformatted(entry.getPacketName());

                    tableNextColumn();
                    textUnformatted(entry.getPacketDataShort());
                }
            }

            if (getScrollY() >= getScrollMaxY()) {
                setScrollHereY(1.0f);
            }

            logClipper.end();
            endTable();
        }
    }

    private void clearLogs() {
        logHistory.clear();
    }

    private void togglePause() {
        logHistory.setPaused(!logHistory.isPaused());
    }

}
