package me.bobulo.mine.pdam.feature.packet.widow;

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

        if (begin(translate("title") + "##PacketLogWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        text(translate("search") + ":");
        sameLine();
        pushItemWidth(300);

        inputText("##search", searchField, ImGuiInputTextFlags.None);

        popItemWidth();

        sameLine();

        dummy(15, 0);

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

        separator();

        sameLine();
        if (button(translate("clear"))) {
            clearLogs();
        }

        sameLine();
        String pauseResumeText = logHistory.isPaused()
          ? translate("resume")
          : translate("pause");
        if (button(pauseResumeText)) {
            togglePause();
        }

        sameLine();
        setNextItemWidth(160.0f);
        inputInt("Max Logs", maxLogs, 1, 100);
        if (isItemDeactivatedAfterEdit()) {
            logHistory.setMaxLogLimit(maxLogs.get());
        }

        text("Total Logs: " + logHistory.size());

        separator();

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
            tableSetupColumn(translate("time"), ImGuiTableColumnFlags.WidthFixed, 85F);
            tableSetupColumn(translate("packet_direcional"), ImGuiTableColumnFlags.WidthFixed, 46F);
            tableSetupColumn(translate("packet_name"), ImGuiTableColumnFlags.WidthFixed, 200F);
            tableSetupColumn(translate("packet_data"), ImGuiTableColumnFlags.WidthStretch);
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
                        entry.setExpanded(!entry.isExpanded());
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
                    if (entry.isExpanded()) {
                        // show full data
                        textWrapped(entry.getPacketData());
                    } else {
                        // show short data
                        textUnformatted(entry.getPacketDataShort());
                    }
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

    private String translate(String key) {
        return translateToLocal("pdam.gui.packet_log." + key);
    }

}
