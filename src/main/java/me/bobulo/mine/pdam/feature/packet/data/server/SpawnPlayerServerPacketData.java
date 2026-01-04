package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.PacketData;
import me.bobulo.mine.pdam.feature.packet.data.entity.PlayerMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class SpawnPlayerServerPacketData implements PacketData {

    private static final String PACKET_NAME = "SpawnPlayer";

    public int entityId;
    public UUID playerId;
    public int x;
    public int y;
    public int z;
    public byte yaw;
    public byte pitch;
    public int currentItem;
    public PlayerMetadata metadata;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }
}
