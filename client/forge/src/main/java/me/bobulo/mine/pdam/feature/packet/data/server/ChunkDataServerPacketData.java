package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ChunkDataServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ChunkData";

    private int chunkX;
    private int chunkZ;
    private boolean groundUpContinuous;
    private Extracted extractedData;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extracted {
        private byte[] data;
        private int dataSize;
    }

    public static class Serializer implements PacketDataSerializer<ChunkDataServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x21);
        }

        @Override
        public @NotNull ChunkDataServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            ChunkDataServerPacketData data = new ChunkDataServerPacketData();
            data.chunkX = buf.readInt();
            data.chunkZ = buf.readInt();
            data.groundUpContinuous = buf.readBoolean();
            data.extractedData = new Extracted();
            data.extractedData.dataSize = buf.readUnsignedShort();
            data.extractedData.data = buf.readByteArray(data.extractedData.dataSize);
            return data;
        }
    }

}


