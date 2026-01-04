package me.bobulo.mine.pdam.feature.packet.log;

import me.bobulo.mine.pdam.feature.packet.data.PacketData;

import java.time.Instant;

public class PacketLogEntry {

    private final Instant timestamp;
    private final PacketData packetData;
    private final PacketDirection direction;

    public PacketLogEntry(Instant timestamp, PacketData packetData, PacketDirection packetDirection) {
        this.timestamp = timestamp;
        this.packetData = packetData;
        this.direction = packetDirection;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public PacketData getPacketData() {
        return packetData;
    }

    public PacketDirection getDirection() {
        return direction;
    }

    public String getPacketName() {
        return packetData.getPacketName();
    }

    public enum PacketDirection {
        SERVER,
        CLIENT
    }

}
