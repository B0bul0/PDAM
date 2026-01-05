package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C18PacketSpectate;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class SpectateClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "Spectate";

    private UUID id;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SpectateClientPacketData, C18PacketSpectate> {

        @Override
        public @NotNull SpectateClientPacketData extract(@NotNull C18PacketSpectate packet) {
            SpectateClientPacketData data = new SpectateClientPacketData();
            data.id = ReflectionUtils.getFieldValue(packet, "id");
            return data;
        }

    }

}

