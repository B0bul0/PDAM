package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import org.jetbrains.annotations.NotNull;

public final class CustomPayloadServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "CustomPayload";

    private String channel;
    private byte[] data;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<CustomPayloadServerPacketData, S3FPacketCustomPayload> {

        @Override
        public @NotNull CustomPayloadServerPacketData extract(@NotNull S3FPacketCustomPayload packet) {
            CustomPayloadServerPacketData data = new CustomPayloadServerPacketData();
            data.channel = packet.getChannelName();

            PacketBuffer packetbuffer = new PacketBuffer(packet.getBufferData().copy());
            int i = packetbuffer.readableBytes();
            if (i >= 0 && i <= 1048576) {
                data.data = new byte[i];
                packetbuffer.readBytes(data.data);
            }

            return data;
        }

    }
}

