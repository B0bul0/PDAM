package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import org.jetbrains.annotations.NotNull;

public final class ParticlesServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Particles";

    private EnumParticleTypes particleType;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    private int[] particleArguments;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ParticlesServerPacketData, S2APacketParticles> {

        @Override
        public @NotNull ParticlesServerPacketData extract(@NotNull S2APacketParticles packet) {
            ParticlesServerPacketData data = new ParticlesServerPacketData();
            data.particleType = packet.getParticleType();
            data.xCoord = (float) packet.getXCoordinate();
            data.yCoord = (float) packet.getYCoordinate();
            data.zCoord = (float) packet.getZCoordinate();
            data.xOffset = packet.getXOffset();
            data.yOffset = packet.getYOffset();
            data.zOffset = packet.getZOffset();
            data.particleSpeed = packet.getParticleSpeed();
            data.particleCount = packet.getParticleCount();
            data.longDistance = packet.isLongDistance();
            data.particleArguments = packet.getParticleArgs();
            return data;
        }

    }

}

