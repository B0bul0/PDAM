package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "Player";

    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean onGround;
    protected boolean moving;
    protected boolean rotating;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<PlayerClientPacketData, C03PacketPlayer> {

        @Override
        public @NotNull PlayerClientPacketData extract(@NotNull C03PacketPlayer packet) {
            PlayerClientPacketData data = new PlayerClientPacketData();
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

