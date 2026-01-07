package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import org.jetbrains.annotations.NotNull;

public final class DestroyEntitiesServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "DestroyEntities";

    private int[] entityIDs;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<DestroyEntitiesServerPacketData, S13PacketDestroyEntities> {

        @Override
        public @NotNull DestroyEntitiesServerPacketData extract(@NotNull S13PacketDestroyEntities packet) {
            DestroyEntitiesServerPacketData data = new DestroyEntitiesServerPacketData();
            data.entityIDs = packet.getEntityIDs();
            return data;
        }

    }

}

