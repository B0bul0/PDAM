package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import org.jetbrains.annotations.NotNull;

public final class KeepAliveServerPacketData implements ServerPacketData {

    private int keepAliveId;

    public int getKeepAliveId() {
        return keepAliveId;
    }

    public static class Extractor implements PacketDataExtractor<KeepAliveServerPacketData, S00PacketKeepAlive> {

        @Override
        public @NotNull KeepAliveServerPacketData extract(@NotNull S00PacketKeepAlive packet) {
            KeepAliveServerPacketData data = new KeepAliveServerPacketData();
            data.keepAliveId = packet.func_149134_c();
            return data;
        }

    }

}