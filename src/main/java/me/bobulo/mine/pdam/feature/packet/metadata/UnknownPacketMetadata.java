package me.bobulo.mine.pdam.feature.packet.metadata;

import org.jetbrains.annotations.NotNull;

public final class UnknownPacketMetadata implements PacketMetadata {

    private final String packetClassName;

    public UnknownPacketMetadata(String packetClassName) {
        this.packetClassName = packetClassName;
    }

    @Override
    public @NotNull String getPacketName() {
        return packetClassName;
    }
}
