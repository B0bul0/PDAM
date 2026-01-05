package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.jetbrains.annotations.NotNull;

public final class ConfirmTransactionClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "ConfirmTransaction";

    private int windowId;
    private short uid;
    private boolean accepted;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ConfirmTransactionClientPacketData, C0FPacketConfirmTransaction> {

        @Override
        public @NotNull ConfirmTransactionClientPacketData extract(@NotNull C0FPacketConfirmTransaction packet) {
            ConfirmTransactionClientPacketData data = new ConfirmTransactionClientPacketData();
            data.windowId = packet.getWindowId();
            data.uid = packet.getUid();
            data.accepted = ReflectionUtils.getFieldValue(packet, "accepted");
            return data;
        }

    }

}

