package me.bobulo.mine.pdam.feature.packet.window;

import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.imgui.window.AbstractPopupRenderItemWindow;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;

public final class PacketInfoWindow extends AbstractPopupRenderItemWindow {

    private static int globalId; // To ensure unique window IDs

    private final DisplayPacketLogEntry entry;
    private final int windowId = globalId++;
    private final ImString packetInfoContent = new ImString(8192);

    private final ImBoolean showRawJson = new ImBoolean(false);

    public PacketInfoWindow(DisplayPacketLogEntry entry) {
        this.entry = entry;
        this.packetInfoContent.set(entry.getPacketData());
    }

    @Override
    public void renderGui() {
        setNextWindowSize(500, 500, ImGuiCond.FirstUseEver);
        setNextWindowPos(60, 60, ImGuiCond.FirstUseEver);

        if (begin("Packet Info##PacketInfoWindow" + windowId, isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        separatorText("Packet Info");

        text("Time: " + entry.getFormattedTime());
        text("Packet Name: " + entry.getPacketName());

        text("Packet Direction:");
        sameLine();
        if (entry.getDirection() == PacketDirection.SERVER) {
            pushStyleColor(ImGuiCol.Text, 0.5f, 1.0f, 0.5f, 1.0f); // Green
            text("SERVER");
        } else {
            pushStyleColor(ImGuiCol.Text, 1.0f, 0.5f, 0.5f, 1.0f); // Red
            text("CLIENT");
        }
        popStyleColor();

        separatorText("Packet Data");

        if (button("Copy to Clipboard")) {
            setClipboardText(packetInfoContent.get());
        }
        sameLine();

        checkbox("Show Raw JSON", showRawJson);

        if (showRawJson.get()) {
            inputTextMultiline("##PacketInfoContent", packetInfoContent,
              -1, -1, ImGuiInputTextFlags.ReadOnly);
        } else {
            if (beginChild("##PacketInfoChild", -1, 0, false)) {
                renderColoredJson(packetInfoContent.get());
            }
            endChild();
        }
    }

    private void renderColoredJson(String prettyJson) {
        if (prettyJson == null || prettyJson.isEmpty()) return;

        String[] lines = prettyJson.split("\n");

        for (String line : lines) {
            int colonIndex = line.indexOf(":");

            if (colonIndex != -1) {
                String keyPart = line.substring(0, colonIndex + 1);
                String valuePart = line.substring(colonIndex + 1);
                boolean hasComma = valuePart.endsWith(",");

                if (hasComma) {
                    valuePart = valuePart.substring(0, valuePart.length() - 1);
                }

                pushStyleColor(ImGuiCol.Text, 0.4f, 0.8f, 1.0f, 1.0f);
                textWrapped(keyPart);
                popStyleColor();

                sameLine(0, 0);

                if (valuePart.contains("\"")) {
                    pushStyleColor(ImGuiCol.Text, 1.0f, 0.6f, 0.4f, 1.0f);
                    textWrapped(valuePart);
                    popStyleColor();
                } else if (valuePart.matches(".*\\d.*")) {
                    pushStyleColor(ImGuiCol.Text, 1.0f, 0.5f, 0.5f, 1.0f);
                    textWrapped(valuePart);
                    popStyleColor();
                } else {
                    pushStyleColor(ImGuiCol.Text, 0.9f, 0.5f, 0.8f, 1.0f);
                    textWrapped(valuePart);
                    popStyleColor();
                }

                if (hasComma) {
                    sameLine(0, 0);
                    textColored(0.7f, 0.7f, 0.7f, 1.0f, ",");
                }
            } else {
                pushStyleColor(ImGuiCol.Text, 0.7f, 0.7f, 0.7f, 1.0f);
                textWrapped(line);
                popStyleColor();
            }
        }
    }

}
