package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ResourcePackStatusClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "ResourcePackStatus";

    private String hash;
    private C19PacketResourcePackStatus.Action status;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<ResourcePackStatusClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x19);
        }

        @Override
        public @NotNull ResourcePackStatusClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            ResourcePackStatusClientPacketData data = new ResourcePackStatusClientPacketData();
            data.hash = buf.readString(40);
            data.status = C19PacketResourcePackStatus.Action.values()[buf.readVarInt()];
            return data;
        }
    }
}

