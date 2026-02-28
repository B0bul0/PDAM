package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class PlayerPosLookServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "PlayerPosLook";

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<S08PacketPlayerPosLook.EnumFlags> flags;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<PlayerPosLookServerPacketData, S08PacketPlayerPosLook> {

        @Override
        public @NotNull PlayerPosLookServerPacketData extract(@NotNull S08PacketPlayerPosLook packet) {

            PlayerPosLookServerPacketData data = new PlayerPosLookServerPacketData();
            data.x = packet.getX();
            data.y = packet.getY();
            data.z = packet.getZ();
            data.yaw = packet.getYaw();
            data.pitch = packet.getPitch();
            data.flags = packet.func_179834_f();
            return data;
        }

    }

}

