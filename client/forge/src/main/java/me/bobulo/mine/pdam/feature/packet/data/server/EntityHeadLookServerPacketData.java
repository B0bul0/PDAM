package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityHeadLookServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityHeadLook";

    private int entityId;
    private byte yaw;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<EntityHeadLookServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x19);
        }

        @Override
        public @NotNull EntityHeadLookServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            EntityHeadLookServerPacketData data = new EntityHeadLookServerPacketData();
            data.entityId = buf.readVarIntFromBuffer();
            data.yaw = buf.readByte();
            return data;
        }
    }

}

