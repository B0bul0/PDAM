package me.bobulo.mine.pdam.feature.packet.data;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import me.bobulo.mine.pdam.feature.packet.data.server.ChatMessageServerPacketData;
import me.bobulo.mine.pdam.feature.packet.data.server.SpawnPlayerServerPacketData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PacketCodecRegistry {

    private static final Map<Class<? extends Packet<?>>, PacketDataExtractor<?, ?>> extractors = new ConcurrentHashMap<>();
    private static final Map<Integer, PacketDataSerializer<?>> serializers = new ConcurrentHashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        registerExtractor(S0CPacketSpawnPlayer.class, new SpawnPlayerServerPacketData.Extractor());
        registerExtractor(S02PacketChat.class, new ChatMessageServerPacketData.Extractor());
    }

    /* Extractor */

    public static <T extends PacketData, E extends Packet<?>> void registerExtractor(Class<E> packetClass, PacketDataExtractor<T, E> extractor) {
        extractors.put(packetClass, extractor);
    }

    public static <T extends PacketData, E extends Packet<?>> PacketDataExtractor<?, ?> getExtractor(Class<E> packetClass) {
        return extractors.get(packetClass);
    }

    public static <T extends PacketData> T extract(Packet<?> packet) throws IOException {
        PacketDataExtractor<T, Packet<?>> extractor = (PacketDataExtractor<T, Packet<?>>) extractors.get(packet.getClass());
        if (extractor != null) {
            return extractor.extract(packet);
        }
        return null;
    }

    /* Serializer */

    public static <T extends PacketData> void registerSerializer(int packetId, PacketDataSerializer<T> serializer) {
        serializers.put(packetId, serializer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketData> PacketDataSerializer<T> getSerializer(int packetId) {
        return (PacketDataSerializer<T>) serializers.get(packetId);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketData> T decode(int packetId, PacketDataBuffer buf) throws IOException {
        PacketDataSerializer<T> serializer = (PacketDataSerializer<T>) serializers.get(packetId);
        if (serializer != null) {
            return serializer.read(buf);
        }
        return null;
    }

}
