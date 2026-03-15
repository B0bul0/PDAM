package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.jetbrains.annotations.NotNull;

public final class TabCompleteServerPacketData implements ServerPacketData {

    private String[] matches;

    public static class Extractor implements PacketDataExtractor<TabCompleteServerPacketData, S3APacketTabComplete> {

        @Override
        public @NotNull TabCompleteServerPacketData extract(@NotNull S3APacketTabComplete packet) {
            TabCompleteServerPacketData data = new TabCompleteServerPacketData();
            data.matches = packet.func_149630_c();
            return data;
        }

    }

}