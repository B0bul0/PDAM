package me.bobulo.mine.pdam.feature.packet.window;

import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.imgui.window.AbstractPopupRenderItemWindow;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public final class PacketInfoWindow extends AbstractPopupRenderItemWindow {

    private static int globalId; // To ensure unique window IDs

    private final DisplayPacketLogEntry entry;
    private final int windowId = globalId++;
    private final ImString packetInfoContent = new ImString(8192);

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

        inputTextMultiline("##PacketInfoContent", packetInfoContent,
          -1, -1, ImGuiInputTextFlags.ReadOnly);
    }

}
