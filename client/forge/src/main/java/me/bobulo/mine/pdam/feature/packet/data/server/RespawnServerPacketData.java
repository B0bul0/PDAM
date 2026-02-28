package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import org.jetbrains.annotations.NotNull;

public final class RespawnServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Respawn";

    private int dimensionID;
    private EnumDifficulty difficulty;
    private WorldSettings.GameType gameType;
    private WorldType worldType;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<RespawnServerPacketData, S07PacketRespawn> {

        @Override
        public @NotNull RespawnServerPacketData extract(@NotNull S07PacketRespawn packet) {
            RespawnServerPacketData data = new RespawnServerPacketData();
            data.dimensionID = packet.getDimensionID();
            data.difficulty = packet.getDifficulty();
            data.gameType = packet.getGameType();
            data.worldType = packet.getWorldType();
            return data;
        }

    }

}

