package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.jetbrains.annotations.NotNull;

public final class ClientStatusClientPacketData implements ClientPacketData {

    private C16PacketClientStatus.EnumState status;

    public static class Extractor implements PacketDataExtractor<ClientStatusClientPacketData, C16PacketClientStatus> {

        @Override
        public @NotNull ClientStatusClientPacketData extract(@NotNull C16PacketClientStatus packet) {
            ClientStatusClientPacketData data = new ClientStatusClientPacketData();
            data.status = packet.getStatus();
            return data;
        }

    }

}