package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.jetbrains.annotations.NotNull;

public final class EntityVelocityServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityVelocity";

    private int entityID;
    private int motionX;
    private int motionY;
    private int motionZ;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityVelocityServerPacketData, S12PacketEntityVelocity> {

        @Override
        public @NotNull EntityVelocityServerPacketData extract(@NotNull S12PacketEntityVelocity packet) {
            EntityVelocityServerPacketData data = new EntityVelocityServerPacketData();

            data.entityID = packet.getEntityID();
            data.motionX = packet.getMotionX();
            data.motionY = packet.getMotionY();
            data.motionZ = packet.getMotionZ();

            return data;
        }

    }

}

