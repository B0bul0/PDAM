package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class TitleServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Title";

    private S45PacketTitle.Type type;
    private IChatComponent message;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<TitleServerPacketData, S45PacketTitle> {

        @Override
        public @NotNull TitleServerPacketData extract(@NotNull S45PacketTitle packet) {
            TitleServerPacketData data = new TitleServerPacketData();
            data.type = packet.getType();
            data.message = packet.getMessage();
            data.fadeIn = packet.getFadeInTime();
            data.stay = packet.getDisplayTime();
            data.fadeOut = packet.getFadeOutTime();
            return data;
        }

    }

}

