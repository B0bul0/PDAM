package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import org.jetbrains.annotations.NotNull;

public final class SpawnExperienceOrbServerPacketData implements ServerPacketData {

    private int entityID;
    private int posX;
    private int posY;
    private int posZ;
    private int xpValue;

    public static class Extractor implements PacketDataExtractor<SpawnExperienceOrbServerPacketData, S11PacketSpawnExperienceOrb> {

        @Override
        public @NotNull SpawnExperienceOrbServerPacketData extract(@NotNull S11PacketSpawnExperienceOrb packet) {
            SpawnExperienceOrbServerPacketData data = new SpawnExperienceOrbServerPacketData();
            data.entityID = packet.getEntityID();
            data.posX = packet.getX();
            data.posY = packet.getY();
            data.posZ = packet.getZ();
            data.xpValue = packet.getXPValue();
            return data;
        }

    }

}