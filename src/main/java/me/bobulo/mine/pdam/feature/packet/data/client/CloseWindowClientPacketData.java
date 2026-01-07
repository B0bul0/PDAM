package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CloseWindowClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "CloseWindow";

    private int windowId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<CloseWindowClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x0D);
        }

        @Override
        public @NotNull CloseWindowClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            CloseWindowClientPacketData data = new CloseWindowClientPacketData();
            data.windowId = buf.readByte();
            return data;
        }

    }

}

