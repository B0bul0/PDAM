package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S0BPacketAnimation;
import org.jetbrains.annotations.NotNull;

public final class AnimationServerPacketData implements ServerPacketData {

    private int entityId;
    private int type;

    public static class Extractor implements PacketDataExtractor<AnimationServerPacketData, S0BPacketAnimation> {

        @Override
        public @NotNull AnimationServerPacketData extract(@NotNull S0BPacketAnimation packet) {
            AnimationServerPacketData data = new AnimationServerPacketData();
            data.entityId = packet.getEntityID();
            data.type = packet.getAnimationType();
            return data;
        }

    }

}