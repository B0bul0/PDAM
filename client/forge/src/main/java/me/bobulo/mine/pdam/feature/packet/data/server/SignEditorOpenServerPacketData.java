package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import org.jetbrains.annotations.NotNull;

public final class SignEditorOpenServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SignEditorOpen";

    private BlockPosition signPosition;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SignEditorOpenServerPacketData, S36PacketSignEditorOpen> {

        @Override
        public @NotNull SignEditorOpenServerPacketData extract(@NotNull S36PacketSignEditorOpen packet) {
            SignEditorOpenServerPacketData data = new SignEditorOpenServerPacketData();
            data.signPosition = BlockPosition.from(packet.getSignPosition());
            return data;
        }

    }

}

