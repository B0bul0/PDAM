package me.bobulo.mine.pdam.feature.packet.data;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class SerializerKey {

    @NotNull
    private final ConnectionState connectionState;

    @NotNull
    private final PacketDirection packetDirection;

    private final int packetId;

    public SerializerKey(@NotNull ConnectionState connectionState, @NotNull PacketDirection packetDirection, int packetId) {
        this.connectionState = connectionState;
        this.packetDirection = packetDirection;
        this.packetId = packetId;
    }

    public @NotNull ConnectionState getConnectionState() {
        return connectionState;
    }

    public @NotNull PacketDirection getPacketDirection() {
        return packetDirection;
    }

    public int getPacketId() {
        return packetId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SerializerKey)) return false;
        SerializerKey that = (SerializerKey) o;
        return packetId == that.packetId &&
          connectionState == that.connectionState &&
          packetDirection == that.packetDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionState, packetDirection, packetId);
    }

}
