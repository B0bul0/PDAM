package me.bobulo.mine.pdam.feature.packet.metadata;

public final class UnknownPacketMetadata implements PacketMetadata {

    private String packetClassName;
    private String data;

    public UnknownPacketMetadata(String packetClassName, String data) {
        this.packetClassName = packetClassName;
        this.data = data;
    }
}
