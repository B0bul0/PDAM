package me.bobulo.mine.pdam.feature.packet.data;

import me.bobulo.mine.pdam.feature.packet.data.entity.PlayerMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class SpawnPlayerPacketData implements PacketData {

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
