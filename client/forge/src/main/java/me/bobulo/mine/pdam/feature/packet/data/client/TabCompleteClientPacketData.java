package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.client.C14PacketTabComplete;
import org.jetbrains.annotations.NotNull;

public final class TabCompleteClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "TabComplete";

    private String message;
    private BlockPosition targetBlock;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<TabCompleteClientPacketData, C14PacketTabComplete> {

        @Override
        public @NotNull TabCompleteClientPacketData extract(@NotNull C14PacketTabComplete packet) {
            TabCompleteClientPacketData data = new TabCompleteClientPacketData();
            data.message = packet.getMessage();
            data.targetBlock = BlockPosition.from(packet.getTargetBlock());
            return data;
        }

    }

}

