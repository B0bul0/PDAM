package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

public final class PlayerPositionLookClientPacketData extends PlayerClientPacketData {

    private static final String PACKET_NAME = "PlayerPositionLook";

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class PositionLookExtractor implements PacketDataExtractor<PlayerPositionLookClientPacketData, C03PacketPlayer.C06PacketPlayerPosLook> {

        @Override
        public @NotNull PlayerPositionLookClientPacketData extract(@NotNull C03PacketPlayer.C06PacketPlayerPosLook packet) {
            PlayerPositionLookClientPacketData data = new PlayerPositionLookClientPacketData();
            data.x = packet.getPositionX();
            data.y = packet.getPositionY();
            data.z = packet.getPositionZ();
            data.yaw = packet.getYaw();
            data.pitch = packet.getPitch();
            data.onGround = packet.isOnGround();
            data.moving = packet.isMoving();
            data.rotating = packet.getRotating();
            return data;
        }

    }

}

