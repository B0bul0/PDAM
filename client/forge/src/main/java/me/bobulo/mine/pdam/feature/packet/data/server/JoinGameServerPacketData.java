package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import org.jetbrains.annotations.NotNull;

public final class JoinGameServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "JoinGame";

    private int entityId;
    private boolean hardcoreMode;
    private WorldSettings.GameType gameType;
    private int dimension;
    private EnumDifficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<JoinGameServerPacketData, S01PacketJoinGame> {

        @Override
        public @NotNull JoinGameServerPacketData extract(@NotNull S01PacketJoinGame packet) {
            JoinGameServerPacketData data = new JoinGameServerPacketData();
            data.entityId = packet.getEntityId();
            data.hardcoreMode = packet.isHardcoreMode();
            data.gameType = packet.getGameType();
            data.dimension = packet.getDimension();
            data.difficulty = packet.getDifficulty();
            data.maxPlayers = packet.getMaxPlayers();
            data.worldType = packet.getWorldType();
            data.reducedDebugInfo = packet.isReducedDebugInfo();
            return data;
        }

    }
}

