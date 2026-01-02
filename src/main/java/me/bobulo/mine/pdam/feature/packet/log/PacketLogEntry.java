package me.bobulo.mine.pdam.feature.packet.log;

import me.bobulo.mine.pdam.feature.packet.metadata.PacketMetadata;

import java.time.Instant;

public class PacketLogEntry {

    private final Instant timestamp;
    private final PacketMetadata packetMetadata;
    private final PacketDirection direction;

    public PacketLogEntry(Instant timestamp, PacketMetadata packetMetadata, PacketDirection packetDirection) {
        this.timestamp = timestamp;
        this.packetMetadata = packetMetadata;
        this.direction = packetDirection;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public PacketMetadata getPacketData() {
        return packetMetadata;
    }

    public PacketDirection getDirection() {
        return direction;
    }

    public String getPacketName() {
        return packetMetadata.getPacketName();
    }

    public enum PacketDirection {
        INCOMING,
        OUTGOING
    }

}
