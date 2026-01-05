package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import org.jetbrains.annotations.NotNull;

public final class SpawnObjectServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SpawnObject";

    private int entityId;
    private int x;
    private int y;
    private int z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private int type;
    private int data;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SpawnObjectServerPacketData, S0EPacketSpawnObject> {

        @Override
        public @NotNull SpawnObjectServerPacketData extract(@NotNull S0EPacketSpawnObject packet) {
            SpawnObjectServerPacketData data = new SpawnObjectServerPacketData();
            data.entityId = packet.getEntityID();
            data.x = packet.getX();
            data.y = packet.getY();
            data.z = packet.getZ();
            data.speedX = packet.getSpeedX();
            data.speedY = packet.getSpeedY();
            data.speedZ = packet.getSpeedZ();
            data.pitch = packet.getPitch();
            data.yaw = packet.getYaw();
            data.type = packet.getType();
            data.data = packet.func_149009_m();
            return data;
        }

    }

}

