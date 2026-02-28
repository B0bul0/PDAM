package me.bobulo.mine.pdam.feature.packet.data.reader;

import me.bobulo.mine.pdam.feature.packet.data.PacketData;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Extracts structured {@link PacketData} from a Minecraft {@link Packet} object.
 *
 * @param <T> the type of packet data to extract
 * @param <E> the type of Minecraft packet to extract from
 * @see PacketDataSerializer
 */
public interface PacketDataExtractor<T extends PacketData, E extends Packet<?>> {

    /**
     * Extracts packet data from the provided Minecraft packet.
     *
     * @param packet the Minecraft packet to extract data from
     * @return the extracted packet data
     * @throws IOException if an error occurs during extraction
     */
    @NotNull T extract(@NotNull E packet) throws IOException;

}