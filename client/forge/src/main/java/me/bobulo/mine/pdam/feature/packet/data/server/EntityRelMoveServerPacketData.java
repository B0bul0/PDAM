package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityRelMoveServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityRelMove";

    private int entityId;
    private byte dx;
    private byte dy;
    private byte dz;
    private boolean onGround;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<EntityRelMoveServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x15);
        }

        @Override
        public @NotNull EntityRelMoveServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            EntityRelMoveServerPacketData data = new EntityRelMoveServerPacketData();
            data.entityId = buf.readVarIntFromBuffer();
            data.dx = buf.readByte();
            data.dy = buf.readByte();
            data.dz = buf.readByte();
            data.onGround = buf.readBoolean();
            return data;
        }

    }
}

