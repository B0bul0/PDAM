package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.jetbrains.annotations.NotNull;

public final class CloseWindowClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "CloseWindow";

    private int windowId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<CloseWindowClientPacketData, C0DPacketCloseWindow> {

        @Override
        public @NotNull CloseWindowClientPacketData extract(@NotNull C0DPacketCloseWindow packet) {
            CloseWindowClientPacketData data = new CloseWindowClientPacketData();
            data.windowId = ReflectionUtils.getFieldValue(packet, "windowId");
            return data;
        }

    }

}

