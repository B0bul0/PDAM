package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class UpdateSignServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "UpdateSign";

    private BlockPosition pos;
    private String[] lines;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<UpdateSignServerPacketData, S33PacketUpdateSign> {

        @Override
        public @NotNull UpdateSignServerPacketData extract(@NotNull S33PacketUpdateSign packet) {
            UpdateSignServerPacketData data = new UpdateSignServerPacketData();
            data.pos = BlockPosition.from(packet.getPos());

            data.lines = new String[4];
            for (int i = 0; i < 4; i++) {
                IChatComponent line = packet.getLines()[i];
                data.lines[i] = line != null ? line.getUnformattedText() : "";
            }

            return data;
        }

    }

}

