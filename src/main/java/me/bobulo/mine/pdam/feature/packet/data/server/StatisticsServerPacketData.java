package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S37PacketStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public final class StatisticsServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Statistics";

    private Map<String, Integer> stats;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<StatisticsServerPacketData, S37PacketStatistics> {

        @Override
        public @NotNull StatisticsServerPacketData extract(@NotNull S37PacketStatistics packet) {
            StatisticsServerPacketData data = new StatisticsServerPacketData();
            data.stats = packet.func_148974_c().entrySet().stream()
              .collect(Collectors.toMap(entry -> entry.getKey().statId, Map.Entry::getValue));
            return data;
        }

    }

}

