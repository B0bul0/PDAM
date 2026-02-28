package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityLookServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityLook";

    private int entityId;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<EntityLookServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x16);
        }

        @Override
        public @NotNull EntityLookServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            EntityLookServerPacketData data = new EntityLookServerPacketData();
            data.entityId = buf.readVarIntFromBuffer();
            data.yaw = buf.readByte();
            data.pitch = buf.readByte();
            data.onGround = buf.readBoolean();
            return data;
        }

    }
}

