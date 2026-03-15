package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import org.jetbrains.annotations.NotNull;

public final class WindowPropertyServerPacketData implements ServerPacketData {

    private int windowId;
    private int varIndex;
    private int varValue;

    public static class Extractor implements PacketDataExtractor<WindowPropertyServerPacketData, S31PacketWindowProperty> {

        @Override
        public @NotNull WindowPropertyServerPacketData extract(@NotNull S31PacketWindowProperty packet) {
            WindowPropertyServerPacketData data = new WindowPropertyServerPacketData();
            data.windowId = packet.getWindowId();
            data.varIndex = packet.getVarIndex();
            data.varValue = packet.getVarValue();
            return data;
        }

    }

}