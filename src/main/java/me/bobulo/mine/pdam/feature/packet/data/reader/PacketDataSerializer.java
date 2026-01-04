package me.bobulo.mine.pdam.feature.packet.data.reader;

import me.bobulo.mine.pdam.feature.packet.data.PacketData;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserializes raw packet data from a {@link PacketDataBuffer} into structured {@link PacketData} objects.
 * <p>
 * This codec reads data directly from the byte buffer without requiring access to the original
 * Minecraft {@link net.minecraft.network.Packet} object.
 * </p>
 *
 * @param <T> the type of packet data to deserialize
 * @see PacketDataExtractor
 * @see PacketDataBuffer
 */
public interface PacketDataSerializer<T extends PacketData> extends PacketDataReader<T> {

    /**
     * Gets the unique packet ID for this serializer.
     *
     * @return the packet ID
     */
    int getPacketId();

    /**
     * Reads and deserializes packet data from the provided buffer.
     *
     * @param buf the buffer containing raw packet data
     * @return the deserialized packet data
     * @throws IOException if an error occurs during deserialization
     */
    @NotNull T read(@NotNull PacketDataBuffer buf) throws IOException;

}