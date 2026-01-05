package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class MapChunkBulkServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "MapChunkBulk";

    private boolean isOverworld;
    private int[] xChunk;
    private int[] zChunk;
    private Extracted[] chunksData;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extracted {
        private byte[] data;
        private int dataSize;
    }

    public static class Serializer implements PacketDataSerializer<MapChunkBulkServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x26);
        }

        @Override
        public @NotNull MapChunkBulkServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            MapChunkBulkServerPacketData data = new MapChunkBulkServerPacketData();

            data.isOverworld = buf.readBoolean();
            int count = buf.readVarInt();
            data.xChunk = new int[count];
            data.zChunk = new int[count];
            data.chunksData = new Extracted[count];

            for (int i = 0; i < count; ++i) {
                data.xChunk[i] = buf.readInt();
                data.zChunk[i] = buf.readInt();
                data.chunksData[i] = new Extracted();
                data.chunksData[i].dataSize = buf.readUnsignedShort();
            }

            for (int i = 0; i < count; ++i) {
                byte[] chunkBytes = new byte[calculateDataSize(data.chunksData[i].dataSize, data.isOverworld)];
                buf.readBytes(chunkBytes);
                data.chunksData[i].data = chunkBytes;
            }

            return data;
        }

        private int calculateDataSize(int dataSize, boolean isOverworld) {
            int setBits = Integer.bitCount(dataSize);
            int i = setBits * 2 * 16 * 16 * 16;
            int j = setBits * 16 * 16 * 16 / 2;
            int k = isOverworld ? setBits * 16 * 16 * 16 / 2 : 0;
            return i + j + k;
        }
    }

}


