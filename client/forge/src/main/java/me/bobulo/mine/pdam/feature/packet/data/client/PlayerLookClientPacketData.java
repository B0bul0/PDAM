package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

public final class PlayerLookClientPacketData extends PlayerClientPacketData {

    private static final String PACKET_NAME = "PlayerLook";

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class LookExtractor implements PacketDataExtractor<PlayerLookClientPacketData, C03PacketPlayer.C05PacketPlayerLook> {

        @Override
        public @NotNull PlayerLookClientPacketData extract(@NotNull C03PacketPlayer.C05PacketPlayerLook packet) {
            PlayerLookClientPacketData data = new PlayerLookClientPacketData();
            data.yaw = packet.getYaw();
            data.pitch = packet.getPitch();
            data.onGround = packet.isOnGround();
            data.moving = packet.isMoving();
            data.rotating = packet.getRotating();
            return data;
        }
    }

}

