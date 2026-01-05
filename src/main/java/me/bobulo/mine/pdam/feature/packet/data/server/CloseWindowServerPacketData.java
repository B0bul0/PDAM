package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CloseWindowServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "CloseWindow";

    private int windowId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<CloseWindowServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x2E);
        }

        @Override
        public @NotNull CloseWindowServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            CloseWindowServerPacketData data = new CloseWindowServerPacketData();
            data.windowId = buf.readUnsignedByte();
            return data;
        }

    }
}

