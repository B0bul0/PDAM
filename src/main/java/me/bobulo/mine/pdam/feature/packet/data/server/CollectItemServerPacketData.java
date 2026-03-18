package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import org.jetbrains.annotations.NotNull;

public final class CollectItemServerPacketData implements ServerPacketData {

    private int collectedItemEntityId;
    private int entityId;

    public static class Extractor implements PacketDataExtractor<CollectItemServerPacketData, S0DPacketCollectItem> {

        @Override
        public @NotNull CollectItemServerPacketData extract(@NotNull S0DPacketCollectItem packet) {
            CollectItemServerPacketData data = new CollectItemServerPacketData();

            data.collectedItemEntityId = packet.getCollectedItemEntityID();
            data.entityId = packet.getEntityID();

            return data;
        }

    }
}