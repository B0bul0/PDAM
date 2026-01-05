package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import org.jetbrains.annotations.NotNull;

public final class ResourcePackSendServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ResourcePackSend";

    private String url;
    private String hash;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ResourcePackSendServerPacketData, S48PacketResourcePackSend> {

        @Override
        public @NotNull ResourcePackSendServerPacketData extract(@NotNull S48PacketResourcePackSend packet) {
            ResourcePackSendServerPacketData data = new ResourcePackSendServerPacketData();
            data.url = packet.getURL();
            data.hash = packet.getHash();
            return data;
        }

    }

}

