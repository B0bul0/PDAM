package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.entity.EntityMetadata;
import me.bobulo.mine.pdam.feature.packet.data.entity.factory.EntityMetadataFactory;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SpawnMobServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SpawnMob";

    private int entityId;
    private int type;
    private int x;
    private int y;
    private int z;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private byte yaw;
    private byte pitch;
    private byte headPitch;
    private EntityMetadata metadata;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SpawnMobServerPacketData, S0FPacketSpawnMob> {

        @Override
        public @NotNull SpawnMobServerPacketData extract(@NotNull S0FPacketSpawnMob packet) throws IOException {
            SpawnMobServerPacketData data = new SpawnMobServerPacketData();
            data.entityId = packet.getEntityID();
            data.type = packet.getEntityType();
            data.x = packet.getX();
            data.y = packet.getY();
            data.z = packet.getZ();
            data.velocityX = packet.getVelocityX();
            data.velocityY = packet.getVelocityY();
            data.velocityZ = packet.getVelocityZ();
            data.yaw = packet.getYaw();
            data.pitch = packet.getPitch();
            data.headPitch = packet.getHeadPitch();
            data.metadata = EntityMetadataFactory.create(packet.func_149027_c());
            return data;
        }

    }

}

