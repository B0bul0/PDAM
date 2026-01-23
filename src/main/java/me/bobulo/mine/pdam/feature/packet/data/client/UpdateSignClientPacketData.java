package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class UpdateSignClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "UpdateSign";

    private BlockPosition pos;
    private IChatComponent[] lines;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<UpdateSignClientPacketData, C12PacketUpdateSign> {

        @Override
        public @NotNull UpdateSignClientPacketData extract(@NotNull C12PacketUpdateSign packet) {
            UpdateSignClientPacketData data = new UpdateSignClientPacketData();
            data.pos = BlockPosition.from(packet.getPosition());
            data.lines = packet.getLines();
            return data;
        }

    }

}

