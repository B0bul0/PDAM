package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import org.jetbrains.annotations.NotNull;

public final class RemoveEntityEffectServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "RemoveEntityEffect";

    private int entityId;
    private int effectId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<RemoveEntityEffectServerPacketData, S1EPacketRemoveEntityEffect> {

        @Override
        public @NotNull RemoveEntityEffectServerPacketData extract(@NotNull S1EPacketRemoveEntityEffect packet) {
            RemoveEntityEffectServerPacketData data = new RemoveEntityEffectServerPacketData();
            data.entityId = packet.getEntityId();
            data.effectId = packet.getEffectId();
            return data;
        }

    }

}

