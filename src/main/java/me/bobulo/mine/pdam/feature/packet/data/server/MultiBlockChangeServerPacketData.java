package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import me.bobulo.mine.pdam.util.BlockPosition;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class MultiBlockChangeServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "MultiBlockChange";

    private ChunkCoordinates chunkPos;
    private BlockUpdateData[] blockUpdates;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class ChunkCoordinates {
        private int x;
        private int z;
    }

    public static class BlockUpdateData {
        private BlockPosition pos;
        private int blockStateId;
    }

    public static class Serializer implements PacketDataSerializer<MultiBlockChangeServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x22);
        }

        @Override
        public @NotNull MultiBlockChangeServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            MultiBlockChangeServerPacketData data = new MultiBlockChangeServerPacketData();
            data.chunkPos = new ChunkCoordinates();
            data.chunkPos.x = buf.readInt();
            data.chunkPos.z = buf.readInt();

            int count = buf.readVarIntFromBuffer();
            data.blockUpdates = new BlockUpdateData[count];

            for (int i = 0; i < count; ++i) {
                short shortPos = buf.readShort();
                int blockStateId = buf.readVarIntFromBuffer();

                BlockUpdateData update = new BlockUpdateData();
                int x = (shortPos >> 12) & 15;
                int y = shortPos & 255;
                int z = (shortPos >> 8) & 15;

                update.pos = new BlockPosition(data.chunkPos.x * 16 + x, y, data.chunkPos.z * 16 + z);
                update.blockStateId = blockStateId;
                data.blockUpdates[i] = update;
            }

            return data;
        }

    }

}


