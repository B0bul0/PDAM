package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class UpdateSignClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "UpdateSign";

    private BlockPos pos;
    private IChatComponent[] lines;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<UpdateSignClientPacketData, C12PacketUpdateSign> {

        @Override
        public @NotNull UpdateSignClientPacketData extract(@NotNull C12PacketUpdateSign packet) {
            UpdateSignClientPacketData data = new UpdateSignClientPacketData();
            data.pos = packet.getPosition();
            data.lines = packet.getLines();
            return data;
        }

    }

}

