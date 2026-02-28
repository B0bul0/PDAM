package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.world.EnumDifficulty;
import org.jetbrains.annotations.NotNull;

public final class ServerDifficultyServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ServerDifficulty";

    private EnumDifficulty difficulty;
    private boolean difficultyLocked;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ServerDifficultyServerPacketData, S41PacketServerDifficulty> {

        @Override
        public @NotNull ServerDifficultyServerPacketData extract(@NotNull S41PacketServerDifficulty packet) {
            ServerDifficultyServerPacketData data = new ServerDifficultyServerPacketData();
            data.difficulty = packet.getDifficulty();
            data.difficultyLocked = packet.isDifficultyLocked();
            return data;
        }

    }

}

