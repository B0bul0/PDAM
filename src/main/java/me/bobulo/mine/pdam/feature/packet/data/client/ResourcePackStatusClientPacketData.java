package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import org.jetbrains.annotations.NotNull;

public final class ResourcePackStatusClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "ResourcePackStatus";

    private String hash;
    private C19PacketResourcePackStatus.Action status;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ResourcePackStatusClientPacketData, C19PacketResourcePackStatus> {
        @Override
        public @NotNull ResourcePackStatusClientPacketData extract(@NotNull C19PacketResourcePackStatus packet) {
            ResourcePackStatusClientPacketData data = new ResourcePackStatusClientPacketData();
            data.hash = ReflectionUtils.getFieldValue(packet, "hash");
            data.status = ReflectionUtils.getFieldValue(packet, "status");
            return data;
        }
    }
}

