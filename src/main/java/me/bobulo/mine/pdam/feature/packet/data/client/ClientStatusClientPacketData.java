package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public final class ClientStatusClientPacketData implements ClientPacketData {

    private static final EnumMap<C16PacketClientStatus.EnumState, State> MAP =
      new EnumMap<>(C16PacketClientStatus.EnumState.class);

    static {
        for (C16PacketClientStatus.EnumState s : C16PacketClientStatus.EnumState.values()) {
            try {
                MAP.put(s, State.valueOf(s.name()));
            } catch (IllegalArgumentException ignored) {
                // ignored, if the enum value is not present in our State enum
            }
        }
    }

    private State status;

    public static class Extractor implements PacketDataExtractor<ClientStatusClientPacketData, C16PacketClientStatus> {

        @Override
        public @NotNull ClientStatusClientPacketData extract(@NotNull C16PacketClientStatus packet) {
            ClientStatusClientPacketData data = new ClientStatusClientPacketData();
            data.status = MAP.get(packet.getStatus());
            return data;
        }

    }

    public enum State {
        PERFORM_RESPAWN,
        REQUEST_STATS,
        OPEN_INVENTORY_ACHIEVEMENT;
    }

}