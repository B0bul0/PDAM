package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ClickWindowClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "ClickWindow";

    private int windowId;
    private int slotId;
    private int usedButton;
    private short actionNumber;
    private ItemStackData clickedItem;
    private int mode;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ClickWindowClientPacketData, C0EPacketClickWindow> {

        @Override
        public @NotNull ClickWindowClientPacketData extract(@NotNull C0EPacketClickWindow packet) throws IOException {
            ClickWindowClientPacketData data = new ClickWindowClientPacketData();
            data.windowId = packet.getWindowId();
            data.slotId = packet.getSlotId();
            data.usedButton = packet.getUsedButton();
            data.actionNumber = packet.getActionNumber();
            data.clickedItem = ItemStackData.from(packet.getClickedItem());
            data.mode = packet.getMode();
            return data;
        }

    }

}

