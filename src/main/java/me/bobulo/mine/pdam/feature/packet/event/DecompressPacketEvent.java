package me.bobulo.mine.pdam.feature.packet.event;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DecompressPacketEvent extends Event {

    private final ConnectionState connectionState;
    private final int packetId;
    private final ByteBuf buffer;

    public DecompressPacketEvent(ConnectionState connectionState, int packetId, ByteBuf buffer) {
        this.connectionState = connectionState;
        this.packetId = packetId;
        this.buffer = buffer;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public int getPacketId() {
        return packetId;
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

}