package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import org.jetbrains.annotations.NotNull;

public final class EntityEffectServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityEffect";

    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte hideParticles;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityEffectServerPacketData, S1DPacketEntityEffect> {

        @Override
        public @NotNull EntityEffectServerPacketData extract(@NotNull S1DPacketEntityEffect packet) {

            EntityEffectServerPacketData data = new EntityEffectServerPacketData();
            data.entityId = packet.getEntityId();
            data.effectId = packet.getEffectId();
            data.amplifier = packet.getAmplifier();
            data.duration = packet.getDuration();
            data.hideParticles = (byte) (packet.func_179707_f() ? 1 : 0);
            return data;
        }

    }
}

