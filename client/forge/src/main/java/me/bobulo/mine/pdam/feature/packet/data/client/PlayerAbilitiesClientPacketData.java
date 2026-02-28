package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class PlayerAbilitiesClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "PlayerAbilities";

    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<PlayerAbilitiesClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x13);
        }

        @Override
        public @NotNull PlayerAbilitiesClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            PlayerAbilitiesClientPacketData data = new PlayerAbilitiesClientPacketData();
            byte flags = buf.readByte();
            data.invulnerable = (flags & 1) != 0;
            data.flying = (flags & 2) != 0;
            data.allowFlying = (flags & 4) != 0;
            data.creativeMode = (flags & 8) != 0;
            data.flySpeed = buf.readFloat();
            data.walkSpeed = buf.readFloat();
            return data;
        }
    }

}

