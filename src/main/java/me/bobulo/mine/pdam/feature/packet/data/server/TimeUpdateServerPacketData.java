package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.jetbrains.annotations.NotNull;

public final class TimeUpdateServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "TimeUpdate";

    private long totalWorldTime;
    private long worldTime;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<TimeUpdateServerPacketData, S03PacketTimeUpdate> {

        @Override
        public @NotNull TimeUpdateServerPacketData extract(@NotNull S03PacketTimeUpdate packet) {
            TimeUpdateServerPacketData data = new TimeUpdateServerPacketData();
            data.totalWorldTime = packet.getTotalWorldTime();
            data.worldTime = packet.getWorldTime();
            return data;
        }

    }

}

