package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public final class PlayerDiggingClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "PlayerDigging";

    private BlockPosition position;
    private EnumFacing facing;
    private C07PacketPlayerDigging.Action status;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<PlayerDiggingClientPacketData, C07PacketPlayerDigging> {

        @Override
        public @NotNull PlayerDiggingClientPacketData extract(@NotNull C07PacketPlayerDigging packet) {
            PlayerDiggingClientPacketData data = new PlayerDiggingClientPacketData();
            data.position = BlockPosition.from(packet.getPosition());
            data.facing = packet.getFacing();
            data.status = packet.getStatus();
            return data;
        }
    }

}

