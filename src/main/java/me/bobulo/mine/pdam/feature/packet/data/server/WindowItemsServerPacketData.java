package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S30PacketWindowItems;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class WindowItemsServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "WindowItems";

    private int windowId;
    private List<ItemStackData> itemStacks;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<WindowItemsServerPacketData, S30PacketWindowItems> {

        @Override
        public @NotNull WindowItemsServerPacketData extract(@NotNull S30PacketWindowItems packet) throws IOException {
            WindowItemsServerPacketData data = new WindowItemsServerPacketData();
            data.windowId = packet.func_148911_c();
            data.itemStacks = new ArrayList<>();
            for (ItemStack itemStack : packet.getItemStacks()) {
                data.itemStacks.add(ItemStackData.from(itemStack));
            }
            return data;
        }

    }

}

