package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

public final class BlockBreakAnimServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "BlockBreakAnim";

    private int breakerId;
    private BlockPos position;
    private int progress;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<BlockBreakAnimServerPacketData, S25PacketBlockBreakAnim> {

        @Override
        public @NotNull BlockBreakAnimServerPacketData extract(@NotNull S25PacketBlockBreakAnim packet) {
            BlockBreakAnimServerPacketData data = new BlockBreakAnimServerPacketData();
            data.breakerId = packet.getBreakerId();
            data.position = packet.getPosition();
            data.progress = packet.getProgress();
            return data;
        }

    }
}

