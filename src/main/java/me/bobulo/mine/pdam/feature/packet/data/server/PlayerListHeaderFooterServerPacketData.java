package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class PlayerListHeaderFooterServerPacketData implements ServerPacketData {

    private IChatComponent header;
    private IChatComponent footer;

    public static class Extractor implements PacketDataExtractor<PlayerListHeaderFooterServerPacketData, S47PacketPlayerListHeaderFooter> {

        @Override
        public @NotNull PlayerListHeaderFooterServerPacketData extract(@NotNull S47PacketPlayerListHeaderFooter packet) {
            PlayerListHeaderFooterServerPacketData data = new PlayerListHeaderFooterServerPacketData();
            data.header = packet.getHeader();
            data.footer = packet.getFooter();
            return data;
        }

    }

}