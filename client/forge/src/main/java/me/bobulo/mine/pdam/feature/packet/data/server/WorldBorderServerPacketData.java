package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class WorldBorderServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "WorldBorder";

    private S44PacketWorldBorder.Action action;
    private int size;
    private double centerX;
    private double centerZ;
    private double targetSize;
    private double diameter;
    private long timeUntilTarget;
    private int warningTime;
    private int warningDistance;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<WorldBorderServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x44);
        }

        @Override
        public @NotNull WorldBorderServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            WorldBorderServerPacketData data = new WorldBorderServerPacketData();
            data.action = buf.readEnum(S44PacketWorldBorder.Action.class);

            switch (data.action) {
                case SET_SIZE:
                    data.diameter = buf.readDouble();
                    break;
                case LERP_SIZE:
                    data.diameter = buf.readDouble();
                    data.targetSize = buf.readDouble();
                    data.timeUntilTarget = buf.readVarLong();
                    break;
                case SET_CENTER:
                    data.centerX = buf.readDouble();
                    data.centerZ = buf.readDouble();
                    break;
                case INITIALIZE:
                    data.centerX = buf.readDouble();
                    data.centerZ = buf.readDouble();
                    data.diameter = buf.readDouble();
                    data.targetSize = buf.readDouble();
                    data.timeUntilTarget = buf.readVarLong();
                    data.size = buf.readVarIntFromBuffer();
                    data.warningDistance = buf.readVarIntFromBuffer();
                    data.warningTime = buf.readVarIntFromBuffer();
                    break;
                case SET_WARNING_TIME:
                    data.warningTime = buf.readVarIntFromBuffer();
                    break;
                case SET_WARNING_BLOCKS:
                    data.warningDistance = buf.readVarIntFromBuffer();
                    break;
            }
            return data;
        }

    }

}

