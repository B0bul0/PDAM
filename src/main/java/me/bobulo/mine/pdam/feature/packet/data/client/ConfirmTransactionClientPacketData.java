package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ConfirmTransactionClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "ConfirmTransaction";

    private int windowId;
    private short uid;
    private boolean accepted;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<ConfirmTransactionClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x0F);
        }

        @Override
        public @NotNull ConfirmTransactionClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            ConfirmTransactionClientPacketData data = new ConfirmTransactionClientPacketData();
            data.windowId = buf.readByte();
            data.uid = buf.readShort();
            data.accepted = buf.readBoolean();
            return data;
        }

    }

}

