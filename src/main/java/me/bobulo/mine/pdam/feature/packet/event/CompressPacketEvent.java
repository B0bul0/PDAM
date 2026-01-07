package me.bobulo.mine.pdam.feature.packet.event;

import io.netty.buffer.ByteBuf;
import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CompressPacketEvent extends Event {

    private final ConnectionState connectionState;
    private final int packetId;
    private final ByteBuf buffer;

    public CompressPacketEvent(ConnectionState connectionState, int packetId, ByteBuf buffer) {
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