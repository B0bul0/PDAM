package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.jetbrains.annotations.NotNull;

public final class ConfirmTransactionServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ConfirmTransaction";

    private int windowId;
    private short actionNumber;
    private boolean accepted;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ConfirmTransactionServerPacketData, S32PacketConfirmTransaction> {

        @Override
        public @NotNull ConfirmTransactionServerPacketData extract(@NotNull S32PacketConfirmTransaction packet) {
            ConfirmTransactionServerPacketData data = new ConfirmTransactionServerPacketData();

            data.windowId = packet.getWindowId();
            data.actionNumber = packet.getActionNumber();
            data.accepted = packet.func_148888_e();

            return data;
        }

    }
}

