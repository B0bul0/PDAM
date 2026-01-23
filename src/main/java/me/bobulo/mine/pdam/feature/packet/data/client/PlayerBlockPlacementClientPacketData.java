package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class PlayerBlockPlacementClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "PlayerBlockPlacement";

    private BlockPosition position;
    private int placedBlockDirection;
    private ItemStackData stack;
    private float facingX;
    private float facingY;
    private float facingZ;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<PlayerBlockPlacementClientPacketData, C08PacketPlayerBlockPlacement> {

        @Override
        public @NotNull PlayerBlockPlacementClientPacketData extract(@NotNull C08PacketPlayerBlockPlacement packet) throws IOException {
            PlayerBlockPlacementClientPacketData data = new PlayerBlockPlacementClientPacketData();
            data.position = BlockPosition.from(packet.getPosition());
            data.placedBlockDirection = packet.getPlacedBlockDirection();
            data.stack = ItemStackData.from(packet.getStack());
            data.facingX = packet.getPlacedBlockOffsetX();
            data.facingY = packet.getPlacedBlockOffsetY();
            data.facingZ = packet.getPlacedBlockOffsetZ();
            return data;
        }

    }

}

