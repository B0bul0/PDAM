package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import me.bobulo.mine.pdam.util.BlockPosition;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class UseBedServerPacketData implements ServerPacketData {

    private int playerID;
    private BlockPosition bedPos;

    public static class Serializer implements PacketDataSerializer<UseBedServerPacketData> {

        @Override
        public @NotNull SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x0A);
        }

        @Override
        public @NotNull UseBedServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            UseBedServerPacketData data = new UseBedServerPacketData();
            data.playerID = buf.readVarIntFromBuffer();
            data.bedPos = BlockPosition.from(buf.readBlockPos());
            return data;
        }

    }

}