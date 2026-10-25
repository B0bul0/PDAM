package me.bobulo.mine.pdam.feature.packet.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.PacketBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacketDataBufferTest {

    private final ByteBuf bytes = Unpooled.buffer();
    private final PacketDataBuffer buffer = new PacketDataBuffer(bytes);

    @AfterEach
    void releaseBuffer() {
        bytes.release();
    }

    @Test
    void readsAMultipleByteVarIntWithoutConsumingTheNextField() {
        bytes.writeBytes(new byte[]{(byte) 0xAC, 0x02, 0x55});

        assertEquals(300, buffer.readVarInt());
        assertEquals(0x55, buffer.readUnsignedByte());
        assertFalse(buffer.isReadable());
    }

    @Test
    void readsABoundedUnicodeStringWithoutConsumingTheNextField() {
        new PacketBuffer(bytes).writeString("ação");
        bytes.writeByte(0x55);

        assertEquals("ação", buffer.readString(4));
        assertEquals(0x55, buffer.readUnsignedByte());
        assertFalse(buffer.isReadable());
    }

    @Test
    void rejectsAStringLongerThanTheCharacterLimit() {
        new PacketBuffer(bytes).writeString("abc");

        assertThrows(DecoderException.class, () -> buffer.readString(2));
    }

    @Test
    void readsAnExplicitByteCountWithoutConsumingALengthPrefix() {
        bytes.writeBytes(new byte[]{2, 3, 4, 5});

        assertArrayEquals(new byte[]{2, 3, 4}, buffer.readByteArray(3));
        assertEquals(5, buffer.readByte());
        assertFalse(buffer.isReadable());
    }

    @Test
    void readsAByteArrayUsingItsEncodedLength() {
        bytes.writeBytes(new byte[]{3, 2, 3, 4, 5});

        assertArrayEquals(new byte[]{2, 3, 4}, buffer.readByteArray());
        assertEquals(5, buffer.readByte());
        assertFalse(buffer.isReadable());
    }

    @Test
    void readsAnExplicitByteCountAsBase64WithoutConsumingALengthPrefix() {
        bytes.writeBytes(new byte[]{2, 3, 4, 5});

        assertEquals("AgME", buffer.readByteArrayAsBase64(3));
        assertEquals(5, buffer.readByte());
        assertFalse(buffer.isReadable());
    }

    @Test
    void readsAByteArrayAsBase64UsingItsEncodedLength() {
        bytes.writeBytes(new byte[]{3, 2, 3, 4, 5});

        assertEquals("AgME", buffer.readByteArrayAsBase64());
        assertEquals(5, buffer.readByte());
        assertFalse(buffer.isReadable());
    }
}
