package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import org.jetbrains.annotations.NotNull;

public final class UpdateScoreServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "UpdateScore";

    private String scoreName;
    private String objectiveName;
    private int value;
    private S3CPacketUpdateScore.Action action;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<UpdateScoreServerPacketData, S3CPacketUpdateScore> {

        @Override
        public @NotNull UpdateScoreServerPacketData extract(@NotNull S3CPacketUpdateScore packet) {
            UpdateScoreServerPacketData data = new UpdateScoreServerPacketData();
            data.scoreName = packet.getPlayerName();
            data.objectiveName = packet.getObjectiveName();
            data.value = packet.getScoreValue();
            data.action = packet.getScoreAction();
            return data;
        }

    }

}

