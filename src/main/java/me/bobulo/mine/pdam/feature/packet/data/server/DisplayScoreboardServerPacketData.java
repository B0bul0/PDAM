package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import org.jetbrains.annotations.NotNull;

public final class DisplayScoreboardServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "DisplayScoreboard";

    private int position;
    private String scoreName;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<DisplayScoreboardServerPacketData, S3DPacketDisplayScoreboard> {

        @Override
        public @NotNull DisplayScoreboardServerPacketData extract(@NotNull S3DPacketDisplayScoreboard packet) {
            DisplayScoreboardServerPacketData data = new DisplayScoreboardServerPacketData();
            data.position = packet.func_149371_c();
            data.scoreName = packet.func_149370_d();
            return data;
        }

    }
}

