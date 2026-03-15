package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import org.jetbrains.annotations.NotNull;

public final class AttachEntityServerPacketData implements ServerPacketData {

    private int leash;
    private int entityId;
    private int vehicleEntityId;

    public static class Extractor implements PacketDataExtractor<AttachEntityServerPacketData, S1BPacketEntityAttach> {

        @Override
        public @NotNull AttachEntityServerPacketData extract(@NotNull S1BPacketEntityAttach packet) {
            AttachEntityServerPacketData data = new AttachEntityServerPacketData();
            data.leash = packet.getLeash();
            data.entityId = packet.getEntityId();
            data.vehicleEntityId = packet.getVehicleEntityId();
            return data;
        }

    }
}