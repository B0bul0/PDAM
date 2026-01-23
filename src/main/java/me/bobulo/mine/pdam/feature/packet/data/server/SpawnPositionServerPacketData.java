package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import org.jetbrains.annotations.NotNull;

public final class SpawnPositionServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SpawnPosition";

    private BlockPosition spawnBlockPos;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SpawnPositionServerPacketData, S05PacketSpawnPosition> {

        @Override
        public @NotNull SpawnPositionServerPacketData extract(@NotNull S05PacketSpawnPosition packet) {
            SpawnPositionServerPacketData data = new SpawnPositionServerPacketData();
            data.spawnBlockPos = BlockPosition.from(packet.getSpawnPos());
            return data;
        }

    }

}

