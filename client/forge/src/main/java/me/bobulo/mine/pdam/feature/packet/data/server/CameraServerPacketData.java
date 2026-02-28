package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CameraServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Camera";

    private int entityId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<CameraServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x43);
        }

        @Override
        public @NotNull CameraServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            CameraServerPacketData data = new CameraServerPacketData();
            data.entityId = buf.readVarIntFromBuffer();
            return data;
        }

    }
}

