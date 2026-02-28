package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityLookMoveServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityLookMove";

    private int entityId;
    private byte dx;
    private byte dy;
    private byte dz;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<EntityLookMoveServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x17);
        }

        @Override
        public @NotNull EntityLookMoveServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            EntityLookMoveServerPacketData data = new EntityLookMoveServerPacketData();
            data.entityId = buf.readVarIntFromBuffer();
            data.dx = buf.readByte();
            data.dy = buf.readByte();
            data.dz = buf.readByte();
            data.yaw = buf.readByte();
            data.pitch = buf.readByte();
            data.onGround = buf.readBoolean();
            return data;
        }

    }
}

