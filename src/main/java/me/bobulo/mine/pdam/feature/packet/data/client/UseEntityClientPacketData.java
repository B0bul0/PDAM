package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class UseEntityClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "UseEntity";

    private int entityId;
    private C02PacketUseEntity.Action action;
    private Vec3 hitVec;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<UseEntityClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x02);
        }

        @Override
        public @NotNull UseEntityClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            UseEntityClientPacketData data = new UseEntityClientPacketData();
            data.entityId = buf.readVarInt();
            data.action = C02PacketUseEntity.Action.values()[buf.readVarInt()];
            if (data.action == C02PacketUseEntity.Action.INTERACT_AT) {
                data.hitVec = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
            }
            return data;
        }

    }

}

