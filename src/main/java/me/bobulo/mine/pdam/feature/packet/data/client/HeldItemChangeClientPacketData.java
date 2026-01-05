package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.jetbrains.annotations.NotNull;

public final class HeldItemChangeClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "HeldItemChange";

    private int slotId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<HeldItemChangeClientPacketData, C09PacketHeldItemChange> {

        @Override
        public @NotNull HeldItemChangeClientPacketData extract(@NotNull C09PacketHeldItemChange packet) {
            HeldItemChangeClientPacketData data = new HeldItemChangeClientPacketData();
            data.slotId = packet.getSlotId();
            return data;
        }
    }

}

