package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import me.bobulo.mine.pdam.util.BlockPosition;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class BlockChangeServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "BlockChange";

    private BlockPosition blockPosition;
    private int blockState;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<BlockChangeServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x23);
        }

        @Override
        public @NotNull BlockChangeServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            BlockChangeServerPacketData data = new BlockChangeServerPacketData();
            data.blockPosition = BlockPosition.from(buf.readBlockPos());
            data.blockState = buf.readVarIntFromBuffer();
            return data;
        }

    }
}

