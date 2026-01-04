package me.bobulo.mine.pdam.feature.packet.event;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DecompressPacketEvent extends Event {

    private final int packetId;
    private final ByteBuf buffer;

    public DecompressPacketEvent(int packetId, ByteBuf buffer) {
        this.packetId = packetId;
        this.buffer = buffer;
    }

    public int getPacketId() {
        return packetId;
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

}