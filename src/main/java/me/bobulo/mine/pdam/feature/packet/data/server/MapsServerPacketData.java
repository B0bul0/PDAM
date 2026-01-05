package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.util.Vec4b;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MapsServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Maps";

    private int mapId;
    private byte mapScale;
    private List<Vec4b> mapVisiblePlayerIcons;
    private int mapMinX;
    private int mapMinY;
    private int mapMaxX;
    private int mapMaxY;
    private byte[] mapDataBytes;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<MapsServerPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.SERVER, 0x34);
        }

        @Override
        public @NotNull MapsServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            MapsServerPacketData data = new MapsServerPacketData();
            data.mapId = buf.readVarInt();
            data.mapScale = buf.readByte();

            data.mapVisiblePlayerIcons = new ArrayList<>();
            int iconCount = buf.readVarInt();
            for (int i = 0; i < iconCount; ++i) {
                byte type = buf.readByte();
                byte x = buf.readByte();
                byte y = buf.readByte();
                byte direction = (byte) (type & 15);
                data.mapVisiblePlayerIcons.add(new Vec4b((byte) (type >> 4 & 15), x, y, direction));
            }

            int columns = buf.readUnsignedByte();
            if (columns > 0) {
                data.mapMinX = columns;
                data.mapMinY = buf.readUnsignedByte();
                data.mapMaxX = buf.readUnsignedByte();
                data.mapMaxY = buf.readUnsignedByte();
                data.mapDataBytes = buf.readByteArray();
            }

            return data;
        }

    }
}

