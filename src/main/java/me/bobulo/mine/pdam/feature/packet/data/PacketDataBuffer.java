package me.bobulo.mine.pdam.feature.packet.data;

import io.netty.buffer.ByteBuf;
import me.bobulo.mine.pdam.feature.packet.data.entity.EntityMetadata;
import me.bobulo.mine.pdam.feature.packet.data.entity.factory.EntityMetadataFactory;
import me.bobulo.mine.pdam.feature.packet.data.item.ItemStackData;
import me.bobulo.mine.pdam.feature.packet.data.nbt.NBTData;
import net.minecraft.entity.DataWatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * A wrapper around PacketBuffer that provides convenient methods for reading packet data.
 */
public class PacketDataBuffer {

    private final PacketBuffer buffer;

    public PacketDataBuffer(ByteBuf byteBuf) {
        this.buffer = new PacketBuffer(byteBuf);
    }

    public PacketDataBuffer(PacketBuffer buffer) {
        this.buffer = buffer;
    }

    /* Primitive Types */

    public byte readByte() {
        return buffer.readByte();
    }

    public short readShort() {
        return buffer.readShort();
    }

    public int readInt() {
        return buffer.readInt();
    }

    public long readLong() {
        return buffer.readLong();
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public int readUnsignedByte() {
        return buffer.readUnsignedByte();
    }

    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    /* Variable Length Types */

    public int readVarInt() {
        return buffer.readVarIntFromBuffer();
    }

    public int readVarIntFromBuffer() {
        return buffer.readVarIntFromBuffer();
    }

    public long readVarLong() {
        return buffer.readVarLong();
    }

    /* String */

    public String readString() {
        return buffer.readStringFromBuffer(32767);
    }

    public String readString(int maxLength) {
        return buffer.readStringFromBuffer(maxLength);
    }

    public String readStringFromBuffer(int maxLength) {
        return buffer.readStringFromBuffer(maxLength);
    }

    /* Medium (3 bytes) */

    public int readMedium() {
        return buffer.readMedium();
    }

    public int readUnsignedMedium() {
        return buffer.readUnsignedMedium();
    }

    /* Other Primitive Types */

    public char readChar() {
        return buffer.readChar();
    }

    public long readUnsignedInt() {
        return buffer.readUnsignedInt();
    }

    /* Arrays */

    public byte[] readByteArray() {
        byte[] bs = new byte[buffer.readVarIntFromBuffer()];
        buffer.readBytes(bs);
        return bs;
    }

    public byte[] readByteArray(int length) {
        byte[] value = new byte[length];
        buffer.readBytes(value);
        return value;
    }

    public String readByteArrayAsBase64() {
        byte[] bs = new byte[buffer.readVarIntFromBuffer()];
        buffer.readBytes(bs);
        return Base64.getEncoder().encodeToString(bs);
    }

    public String readByteArrayAsBase64(int length) {
        byte[] value = new byte[length];
        buffer.readBytes(value);
        return Base64.getEncoder().encodeToString(value);
    }

    public int[] readVarIntArray() {
        int length = buffer.readVarIntFromBuffer();
        int[] value = new int[length];
        for (int i = 0; i < length; i++) {
            value[i] = buffer.readVarIntFromBuffer();
        }
        return value;
    }

    public long[] readLongArray(int length) {
        long[] value = new long[length];
        for (int i = 0; i < length; i++) {
            value[i] = buffer.readLong();
        }
        return value;
    }

    /* Minecraft Types */

    public UUID readUuid() {
        return buffer.readUuid();
    }

    public BlockPos readBlockPos() {
        return buffer.readBlockPos();
    }

    public IChatComponent readChatComponent() throws IOException {
        return buffer.readChatComponent();
    }

    public String readChatComponentAsString() throws IOException {
        IChatComponent component = buffer.readChatComponent();
        return component != null ? component.getUnformattedText() : null;
    }

    public ItemStack readItemStack() throws IOException {
        return buffer.readItemStackFromBuffer();
    }

    public ItemStackData readItemStackAsData() throws IOException {
        ItemStack value = buffer.readItemStackFromBuffer();
        return value != null ? ItemStackData.from(value) : null;
    }

    public NBTTagCompound readNBT() throws IOException {
        return buffer.readNBTTagCompoundFromBuffer();
    }

    public Map<String, Object> readNBTAsData() throws IOException {
        NBTTagCompound value = buffer.readNBTTagCompoundFromBuffer();
        return value != null ? NBTData.from(value) : null;
    }

    public <T extends EntityMetadata> T readEntityMetadata(Class<T> clazz) throws IOException {
        List<DataWatcher.WatchableObject> watchedList = DataWatcher.readWatchedListFromPacketBuffer(buffer);
        if (watchedList != null && !watchedList.isEmpty()) {
            return EntityMetadataFactory.create(watchedList, clazz);
        }
        return null;
    }

    public EntityMetadata readEntityMetadata() throws IOException {
        List<DataWatcher.WatchableObject> watchedList = DataWatcher.readWatchedListFromPacketBuffer(buffer);
        if (watchedList != null && !watchedList.isEmpty()) {
            return EntityMetadataFactory.create(watchedList);
        }
        return null;
    }

    /* Enum */

    public <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        return buffer.readEnumValue(enumClass);
    }

    /* Raw Data */

    public byte[] readRawBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    public String readRawBytesAsBase64(int length) {
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public void skipBytes(int length) {
        buffer.skipBytes(length);
    }

    /* Buffer Info */

    public int readableBytes() {
        return buffer.readableBytes();
    }

    public boolean isReadable() {
        return buffer.isReadable();
    }

    public boolean isReadable(int size) {
        return buffer.isReadable(size);
    }

    public int readerIndex() {
        return buffer.readerIndex();
    }

    public void readerIndex(int index) {
        buffer.readerIndex(index);
    }

    public int writerIndex() {
        return buffer.writerIndex();
    }

    public void writerIndex(int index) {
        buffer.writerIndex(index);
    }

    public void markReaderIndex() {
        buffer.markReaderIndex();
    }

    public void resetReaderIndex() {
        buffer.resetReaderIndex();
    }

    public void markWriterIndex() {
        buffer.markWriterIndex();
    }

    public void resetWriterIndex() {
        buffer.resetWriterIndex();
    }

    public int capacity() {
        return buffer.capacity();
    }

    public int maxCapacity() {
        return buffer.maxCapacity();
    }

    /* Buffer Operations */

    public ByteBuf slice() {
        return buffer.slice();
    }

    public ByteBuf slice(int index, int length) {
        return buffer.slice(index, length);
    }

    public ByteBuf readSlice(int length) {
        return buffer.readSlice(length);
    }

    public ByteBuf duplicate() {
        return buffer.duplicate();
    }

    public ByteBuf readBytes(int length) {
        return buffer.readBytes(length);
    }

    public void clear() {
        buffer.clear();
    }

    public void discardReadBytes() {
        buffer.discardReadBytes();
    }

    public void discardSomeReadBytes() {
        buffer.discardSomeReadBytes();
    }

    /* Buffer Access */

    public PacketBuffer getBuffer() {
        return buffer;
    }

    public ByteBuf unwrap() {
        return buffer.unwrap();
    }

    public boolean hasArray() {
        return buffer.hasArray();
    }

    public byte[] array() {
        return buffer.array();
    }

    public int arrayOffset() {
        return buffer.arrayOffset();
    }

    /* NIO Buffer Methods */

    public ByteBuffer nioBuffer() {
        return buffer.nioBuffer();
    }

    public ByteBuffer nioBuffer(int index, int length) {
        return buffer.nioBuffer(index, length);
    }

    public int nioBufferCount() {
        return buffer.nioBufferCount();
    }

    public ByteBuffer[] nioBuffers() {
        return buffer.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int index, int length) {
        return buffer.nioBuffers(index, length);
    }

    /* Additional Read Methods */

    public void readBytes(byte[] dest) {
        buffer.readBytes(dest);
    }

    public void readBytes(byte[] dest, int destIndex, int length) {
        buffer.readBytes(dest, destIndex, length);
    }

    public void readBytes(ByteBuf dest) {
        buffer.readBytes(dest);
    }

    public void readBytes(ByteBuf dest, int length) {
        buffer.readBytes(dest, length);
    }

    public void readBytes(ByteBuf dest, int destIndex, int length) {
        buffer.readBytes(dest, destIndex, length);
    }

    public void readBytes(java.nio.ByteBuffer dest) {
        buffer.readBytes(dest);
    }

    /* Reference Counting */

    public int refCnt() {
        return buffer.refCnt();
    }

    public boolean release() {
        return buffer.release();
    }

    public boolean release(int decrement) {
        return buffer.release(decrement);
    }

    public PacketDataBuffer retain() {
        buffer.retain();
        return this;
    }

    public PacketDataBuffer retain(int increment) {
        buffer.retain(increment);
        return this;
    }

    /* Utility */

    public PacketDataBuffer copy() {
        return new PacketDataBuffer(buffer.copy());
    }

    public boolean isDirect() {
        return buffer.isDirect();
    }

    public ByteOrder order() {
        return buffer.order();
    }

    @Override
    public String toString() {
        return "PacketDataBuffer{readableBytes=" + readableBytes() + ", readerIndex=" + readerIndex() + "}";
    }

    @Override
    public int hashCode() {
        return buffer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PacketDataBuffer that = (PacketDataBuffer) obj;
        return buffer.equals(that.buffer);
    }
}