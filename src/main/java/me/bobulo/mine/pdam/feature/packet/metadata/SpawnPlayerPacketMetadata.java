package me.bobulo.mine.pdam.feature.packet.metadata;

import java.util.UUID;

public final class SpawnPlayerPacketMetadata implements PacketMetadata {

    public int entityId;
    public UUID playerId;
    public int x;
    public int y;
    public int z;
    public byte yaw;
    public byte pitch;
    public int currentItem;

}
