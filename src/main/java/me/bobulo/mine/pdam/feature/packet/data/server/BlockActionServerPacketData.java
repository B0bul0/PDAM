package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import me.bobulo.mine.pdam.util.BlockPosition;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class BlockActionServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "BlockAction";

    private BlockPosition blockPosition;
    private int instrument;
    private int pitch;
    private int blockType;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<BlockActionServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x24);
        }

        @Override
        public @NotNull BlockActionServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            BlockActionServerPacketData data = new BlockActionServerPacketData();

            data.blockPosition = buf.readBlockPosition();
            data.instrument = buf.readUnsignedByte();
            data.pitch = buf.readUnsignedByte();
            data.blockType = buf.readVarIntFromBuffer();

            return data;
        }

    }
}

