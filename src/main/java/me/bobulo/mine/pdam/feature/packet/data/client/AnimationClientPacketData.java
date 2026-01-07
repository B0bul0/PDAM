package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.jetbrains.annotations.NotNull;

public final class AnimationClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "Animation";

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<AnimationClientPacketData, C0APacketAnimation> {

        @Override
        public @NotNull AnimationClientPacketData extract(@NotNull C0APacketAnimation packet) {
            return new AnimationClientPacketData();
        }

    }

}

