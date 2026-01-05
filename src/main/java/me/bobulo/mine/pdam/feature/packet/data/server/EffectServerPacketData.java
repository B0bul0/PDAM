package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

public final class EffectServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Effect";

    private int soundType;
    private BlockPos soundPos;
    private int soundData;
    private boolean serverWide;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EffectServerPacketData, S28PacketEffect> {

        @Override
        public @NotNull EffectServerPacketData extract(@NotNull S28PacketEffect packet) {
            EffectServerPacketData data = new EffectServerPacketData();
            data.soundType = packet.getSoundType();
            data.soundPos = packet.getSoundPos();
            data.soundData = packet.getSoundData();
            data.serverWide = packet.isSoundServerwide();
            return data;
        }

    }
}

