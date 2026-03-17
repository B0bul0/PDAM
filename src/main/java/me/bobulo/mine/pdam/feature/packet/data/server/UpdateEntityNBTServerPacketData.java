package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public final class UpdateEntityNBTServerPacketData implements ServerPacketData {

    private int entityId;
    private Map<String, Object> tagCompound;

    public static class Serializer implements PacketDataSerializer<UpdateEntityNBTServerPacketData> {

        @Override
        public @NotNull SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x49);
        }

        @Override
        public @NotNull UpdateEntityNBTServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            UpdateEntityNBTServerPacketData data = new UpdateEntityNBTServerPacketData();

            data.entityId = buf.readVarInt();
            data.tagCompound = buf.readNBTAsData();

            return data;
        }

    }

}