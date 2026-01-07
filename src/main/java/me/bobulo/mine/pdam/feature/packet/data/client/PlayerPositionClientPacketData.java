package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

public final class PlayerPositionClientPacketData extends PlayerClientPacketData {

    private static final String PACKET_NAME = "PlayerPosition";

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class PositionExtractor implements PacketDataExtractor<PlayerPositionClientPacketData, C03PacketPlayer.C04PacketPlayerPosition> {

        @Override
        public @NotNull PlayerPositionClientPacketData extract(@NotNull C03PacketPlayer.C04PacketPlayerPosition packet) {
            PlayerPositionClientPacketData data = new PlayerPositionClientPacketData();
            data.x = packet.getPositionX();
            data.y = packet.getPositionY();
            data.z = packet.getPositionZ();
            data.onGround = packet.isOnGround();
            data.moving = packet.isMoving();
            data.rotating = packet.getRotating();
            return data;
        }
    }

}

