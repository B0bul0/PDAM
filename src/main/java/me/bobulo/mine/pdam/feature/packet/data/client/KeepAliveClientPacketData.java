package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import org.jetbrains.annotations.NotNull;

public final class KeepAliveClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "KeepAlive";

    private int key;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<KeepAliveClientPacketData, C00PacketKeepAlive> {

        @Override
        public @NotNull KeepAliveClientPacketData extract(@NotNull C00PacketKeepAlive packet) {
            KeepAliveClientPacketData data = new KeepAliveClientPacketData();
            data.key = packet.getKey();
            return data;
        }

    }

}

