package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import org.jetbrains.annotations.NotNull;

public final class OpenWindowServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "OpenWindow";

    private int windowId;
    private String inventoryType;
    private String windowTitle;
    private int slotCount;
    private int entityId;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<OpenWindowServerPacketData, S2DPacketOpenWindow> {

        @Override
        public @NotNull OpenWindowServerPacketData extract(@NotNull S2DPacketOpenWindow packet) {
            OpenWindowServerPacketData data = new OpenWindowServerPacketData();
            data.windowId = packet.getWindowId();
            data.inventoryType = packet.getGuiId();
            data.windowTitle = packet.getWindowTitle().getUnformattedText();
            data.slotCount = packet.getSlotCount();
            data.entityId = packet.getEntityId();
            return data;
        }

    }

}

