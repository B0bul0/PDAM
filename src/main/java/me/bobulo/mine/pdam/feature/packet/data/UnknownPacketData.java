package me.bobulo.mine.pdam.feature.packet.data;

import org.jetbrains.annotations.NotNull;

public final class UnknownPacketData implements PacketData {

    @NotNull
    private final String packetClassName;

    public UnknownPacketData(@NotNull String packetClassName) {
        this.packetClassName = packetClassName;
    }

    public @NotNull String getPacketClassName() {
        return packetClassName;
    }
}
