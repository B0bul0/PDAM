package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import org.jetbrains.annotations.NotNull;

public final class ChangeGameStateServerPacketData implements ServerPacketData {

    private int state;
    private float value;

    public static class Extractor implements PacketDataExtractor<ChangeGameStateServerPacketData, S2BPacketChangeGameState> {

        @Override
        public @NotNull ChangeGameStateServerPacketData extract(@NotNull S2BPacketChangeGameState packet) {
            ChangeGameStateServerPacketData data = new ChangeGameStateServerPacketData();
            data.state = packet.getGameState();
            data.value = packet.func_149137_d();
            return data;
        }

    }
}