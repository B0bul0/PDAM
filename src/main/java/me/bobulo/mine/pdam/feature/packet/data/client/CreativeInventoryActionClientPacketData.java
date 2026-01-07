package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CreativeInventoryActionClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "CreativeInventoryAction";

    private int slotId;
    private ItemStackData stack;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<CreativeInventoryActionClientPacketData, C10PacketCreativeInventoryAction> {

        @Override
        public @NotNull CreativeInventoryActionClientPacketData extract(@NotNull C10PacketCreativeInventoryAction packet) throws IOException {
            CreativeInventoryActionClientPacketData data = new CreativeInventoryActionClientPacketData();
            data.slotId = packet.getSlotId();
            data.stack = ItemStackData.from(packet.getStack());
            return data;
        }

    }

}

