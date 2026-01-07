package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ExplosionServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Explosion";

    private double x;
    private double y;
    private double z;
    private float strength;
    private List<BlockPos> affectedBlockPositions;
    private float motionX;
    private float motionY;
    private float motionZ;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ExplosionServerPacketData, S27PacketExplosion> {

        @Override
        public @NotNull ExplosionServerPacketData extract(@NotNull S27PacketExplosion packet) {
            ExplosionServerPacketData data = new ExplosionServerPacketData();

            data.x = packet.getX();
            data.y = packet.getY();
            data.z = packet.getZ();
            data.strength = packet.getStrength();
            data.affectedBlockPositions = packet.getAffectedBlockPositions();
            data.motionX = packet.func_149149_c();
            data.motionY = packet.func_149144_d();
            data.motionZ = packet.func_149147_e();

            return data;
        }

    }
}

