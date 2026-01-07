package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityActionClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "EntityAction";

    private int entityID;
    private C0BPacketEntityAction.Action action;
    private int auxData;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<EntityActionClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x0B);
        }

        @Override
        public @NotNull EntityActionClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            EntityActionClientPacketData data = new EntityActionClientPacketData();
            data.entityID = buf.readVarInt();
            data.action = C0BPacketEntityAction.Action.values()[buf.readByte()];
            data.auxData = buf.readVarInt();
            return data;
        }

    }

}

