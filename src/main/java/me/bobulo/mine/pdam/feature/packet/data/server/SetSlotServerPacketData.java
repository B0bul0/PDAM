package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SetSlotServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SetSlot";

    private int windowId;
    private int slot;
    private ItemStackData item;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SetSlotServerPacketData, S2FPacketSetSlot> {

        @Override
        public @NotNull SetSlotServerPacketData extract(@NotNull S2FPacketSetSlot packet) throws IOException {

            SetSlotServerPacketData data = new SetSlotServerPacketData();
            data.windowId = packet.func_149175_c();
            data.slot = packet.func_149173_d();
            data.item = ItemStackData.from(packet.func_149174_e());
            return data;
        }

    }

}

