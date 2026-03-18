package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public final class SpectateClientPacketData implements ClientPacketData {

    private UUID id;

    public static class Serializer implements PacketDataSerializer<SpectateClientPacketData> {

        @Override
        public @NotNull SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x18);
        }

        @Override
        public @NotNull SpectateClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            SpectateClientPacketData data = new SpectateClientPacketData();
            data.id = buf.readUuid();
            return data;
        }

    }

}