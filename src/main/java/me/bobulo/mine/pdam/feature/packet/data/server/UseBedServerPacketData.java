package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class UseBedServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "UseBed";

    private int playerID;
    private BlockPos bedPos;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<UseBedServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x0A);
        }

        @Override
        public @NotNull UseBedServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            UseBedServerPacketData data = new UseBedServerPacketData();
            data.playerID = buf.readVarIntFromBuffer();
            data.bedPos = buf.readBlockPos();
            return data;
        }

    }

}

