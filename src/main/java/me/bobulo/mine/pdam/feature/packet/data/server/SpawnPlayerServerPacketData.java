package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.entity.PlayerMetadata;
import me.bobulo.mine.pdam.feature.packet.data.entity.factory.EntityMetadataFactory;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public final class SpawnPlayerServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SpawnPlayer";

    private int entityId;
    private UUID playerId;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private int currentItem;
    private PlayerMetadata metadata;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Serializer implements PacketDataSerializer<SpawnPlayerServerPacketData> {

        @Override
        public int getPacketId() {
            return 0x0C;
        }

        @Override
        public @NotNull SpawnPlayerServerPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            SpawnPlayerServerPacketData data = new SpawnPlayerServerPacketData();

            data.entityId = buf.readVarIntFromBuffer();
            data.playerId = buf.readUuid();
            data.x = buf.readInt();
            data.y = buf.readInt();
            data.z = buf.readInt();
            data.yaw = buf.readByte();
            data.pitch = buf.readByte();
            data.currentItem = buf.readShort();
            data.metadata = buf.readEntityMetadata(PlayerMetadata.class);

            return data;
        }
    }

    public static class Extractor implements PacketDataExtractor<SpawnPlayerServerPacketData, S0CPacketSpawnPlayer> {

        @Override
        public @NotNull SpawnPlayerServerPacketData extract(@NotNull S0CPacketSpawnPlayer packet) {
            SpawnPlayerServerPacketData data = new SpawnPlayerServerPacketData();

            data.entityId = packet.getEntityID();
            data.playerId = packet.getPlayer();
            data.x = packet.getX();
            data.y = packet.getY();
            data.z = packet.getZ();
            data.yaw = packet.getYaw();
            data.pitch = packet.getPitch();
            data.currentItem = packet.getCurrentItemID();
            data.metadata = EntityMetadataFactory.create(packet.func_148944_c(), PlayerMetadata.class);

            return data;
        }
    }

}
