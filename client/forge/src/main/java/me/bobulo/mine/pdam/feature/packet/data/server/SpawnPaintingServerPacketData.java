package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public final class SpawnPaintingServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SpawnPainting";

    private int entityID;
    private BlockPosition position;
    private EnumFacing facing;
    private String title;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SpawnPaintingServerPacketData, S10PacketSpawnPainting> {

        @Override
        public @NotNull SpawnPaintingServerPacketData extract(@NotNull S10PacketSpawnPainting packet) {
            SpawnPaintingServerPacketData data = new SpawnPaintingServerPacketData();
            data.entityID = packet.getEntityID();
            data.position = BlockPosition.from(packet.getPosition());
            data.facing = packet.getFacing();
            data.title = packet.getTitle();
            return data;
        }

    }

}

