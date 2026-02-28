package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import org.jetbrains.annotations.NotNull;

public final class HeldItemChangeServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "HeldItemChange";

    private int heldItemHotbarSlot;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<HeldItemChangeServerPacketData, S09PacketHeldItemChange> {

        @Override
        public @NotNull HeldItemChangeServerPacketData extract(@NotNull S09PacketHeldItemChange packet) {
            HeldItemChangeServerPacketData data = new HeldItemChangeServerPacketData();
            data.heldItemHotbarSlot = packet.getHeldItemHotbarIndex();
            return data;
        }

    }

}

