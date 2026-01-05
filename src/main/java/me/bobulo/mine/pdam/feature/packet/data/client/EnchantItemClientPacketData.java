package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import org.jetbrains.annotations.NotNull;

public final class EnchantItemClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "EnchantItem";

    private int windowId;
    private int button;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EnchantItemClientPacketData, C11PacketEnchantItem> {

        @Override
        public @NotNull EnchantItemClientPacketData extract(@NotNull C11PacketEnchantItem packet) {
            EnchantItemClientPacketData data = new EnchantItemClientPacketData();
            data.windowId = packet.getWindowId();
            data.button = packet.getButton();
            return data;
        }

    }
}

