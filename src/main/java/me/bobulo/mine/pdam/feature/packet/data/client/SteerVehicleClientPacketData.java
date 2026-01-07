package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C0CPacketInput;
import org.jetbrains.annotations.NotNull;

public final class SteerVehicleClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "SteerVehicle";

    private float strafeSpeed;
    private float forwardSpeed;
    private boolean jumping;
    private boolean sneaking;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SteerVehicleClientPacketData, C0CPacketInput> {

        @Override
        public @NotNull SteerVehicleClientPacketData extract(@NotNull C0CPacketInput packet) {
            SteerVehicleClientPacketData data = new SteerVehicleClientPacketData();
            data.strafeSpeed = packet.getStrafeSpeed();
            data.forwardSpeed = packet.getForwardSpeed();
            data.jumping = packet.isJumping();
            data.sneaking = packet.isSneaking();
            return data;
        }

    }

}

