package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

public final class EntityStatusServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityStatus";

    private int entityId;
    private byte logicOpcode;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<EntityStatusServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x1A);
        }

        @Override
        public @NotNull EntityStatusServerPacketData read(@NotNull PacketDataBuffer buf) throws java.io.IOException {
            EntityStatusServerPacketData data = new EntityStatusServerPacketData();
            data.entityId = buf.readInt();
            data.logicOpcode = buf.readByte();
            return data;
        }

    }

}

