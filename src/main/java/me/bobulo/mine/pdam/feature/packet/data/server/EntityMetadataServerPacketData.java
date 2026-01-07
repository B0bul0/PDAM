package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.entity.EntityMetadata;
import me.bobulo.mine.pdam.feature.packet.data.entity.factory.EntityMetadataFactory;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityMetadataServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityMetadata";

    private int entityId;
    private EntityMetadata metadata;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityMetadataServerPacketData, S1CPacketEntityMetadata> {

        @Override
        public @NotNull EntityMetadataServerPacketData extract(@NotNull S1CPacketEntityMetadata packet) throws IOException {
            EntityMetadataServerPacketData data = new EntityMetadataServerPacketData();
            data.entityId = packet.getEntityId();
            data.metadata = packet.func_149376_c() == null ? null : EntityMetadataFactory.create(packet.func_149376_c());
            return data;
        }

    }
}

