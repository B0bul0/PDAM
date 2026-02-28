package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import org.jetbrains.annotations.NotNull;

public final class EntityTeleportServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityTeleport";

    private int entityId;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityTeleportServerPacketData, S18PacketEntityTeleport> {

        @Override
        public @NotNull EntityTeleportServerPacketData extract(@NotNull S18PacketEntityTeleport packet) {
            EntityTeleportServerPacketData data = new EntityTeleportServerPacketData();
            data.entityId = packet.getEntityId();
            data.x = packet.getX();
            data.y = packet.getY();
            data.z = packet.getZ();
            data.yaw = packet.getYaw();
            data.pitch = packet.getPitch();
            data.onGround = packet.getOnGround();
            return data;
        }
    }

}

