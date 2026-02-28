package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class EntityEquipmentServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityEquipment";

    private int entityID;
    private int slot;
    private ItemStackData itemStack;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityEquipmentServerPacketData, S04PacketEntityEquipment> {

        @Override
        public @NotNull EntityEquipmentServerPacketData extract(@NotNull S04PacketEntityEquipment packet) throws IOException {
            EntityEquipmentServerPacketData data = new EntityEquipmentServerPacketData();
            data.entityID = packet.getEntityID();
            data.slot = packet.getEquipmentSlot();
            data.itemStack = ItemStackData.from(packet.getItemStack());
            return data;
        }

    }
}

