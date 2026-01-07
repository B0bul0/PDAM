package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import org.jetbrains.annotations.NotNull;

public final class ScoreboardObjectiveServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ScoreboardObjective";

    private String objectiveName;
    private String objectiveValue;
    private IScoreObjectiveCriteria.EnumRenderType type;
    private int action;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ScoreboardObjectiveServerPacketData, S3BPacketScoreboardObjective> {

        @Override
        public @NotNull ScoreboardObjectiveServerPacketData extract(@NotNull S3BPacketScoreboardObjective packet) {
            ScoreboardObjectiveServerPacketData data = new ScoreboardObjectiveServerPacketData();
            data.objectiveName = packet.func_149339_c();
            data.objectiveValue = packet.func_149337_d();
            data.type = packet.func_179817_d();
            data.action = packet.func_149338_e();
            return data;
        }

    }

}

