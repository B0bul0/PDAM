package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class DisconnectServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Disconnect";

    private IChatComponent reason;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<DisconnectServerPacketData, S40PacketDisconnect> {

        @Override
        public @NotNull DisconnectServerPacketData extract(@NotNull S40PacketDisconnect packet) {
            DisconnectServerPacketData data = new DisconnectServerPacketData();
            data.reason = packet.getReason();
            return data;
        }

    }
}

