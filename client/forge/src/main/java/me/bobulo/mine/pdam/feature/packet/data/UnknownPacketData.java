package me.bobulo.mine.pdam.feature.packet.data;

import org.jetbrains.annotations.NotNull;

public final class UnknownPacketData implements PacketData {

    private final String packetClassName;

    public UnknownPacketData(String packetClassName) {
        this.packetClassName = packetClassName;
    }

    @Override
    public @NotNull String getPacketName() {
        return packetClassName;
    }
}
