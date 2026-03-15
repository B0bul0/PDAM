package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.jetbrains.annotations.NotNull;

public final class PluginMessageClientPacketData implements ClientPacketData {

    private String channel;
    private byte[] data;

    public static class Extractor implements PacketDataExtractor<PluginMessageClientPacketData, C17PacketCustomPayload> {

        @Override
        public @NotNull PluginMessageClientPacketData extract(@NotNull C17PacketCustomPayload packet) {
            PluginMessageClientPacketData data = new PluginMessageClientPacketData();
            data.channel = packet.getChannelName();
            PacketBuffer packetbuffer = new PacketBuffer(packet.getBufferData().copy());
            int i = packetbuffer.readableBytes();
            if (i >= 0 && i <= 32767) {
                data.data = new byte[i];
                packetbuffer.readBytes(data.data);
            }
            return data;
        }

    }
}