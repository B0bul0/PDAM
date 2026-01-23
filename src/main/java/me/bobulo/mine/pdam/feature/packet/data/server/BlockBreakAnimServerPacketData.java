package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import org.jetbrains.annotations.NotNull;

public final class BlockBreakAnimServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "BlockBreakAnim";

    private int breakerId;
    private BlockPosition position;
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
            data.position = BlockPosition.from(packet.getPosition());
            data.progress = packet.getProgress();
            return data;
        }

    }
}

