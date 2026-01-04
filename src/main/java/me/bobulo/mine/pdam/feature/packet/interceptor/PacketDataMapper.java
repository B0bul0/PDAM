package me.bobulo.mine.pdam.feature.packet.interceptor;

import me.bobulo.mine.pdam.feature.packet.data.PacketData;
import me.bobulo.mine.pdam.feature.packet.data.ChatMessagePacketData;
import me.bobulo.mine.pdam.feature.packet.data.SpawnPlayerPacketData;
import me.bobulo.mine.pdam.feature.packet.data.entity.factory.EntityMetadataFactory;
import me.bobulo.mine.pdam.feature.packet.data.entity.PlayerMetadata;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.jetbrains.annotations.Nullable;

public final class PacketDataMapper {

    @Nullable
    public static PacketData map(Packet<?> packet) {
        if (packet instanceof S02PacketChat) {
            S02PacketChat chatPacket = (S02PacketChat) packet;
            return new ChatMessagePacketData(
              chatPacket.getChatComponent().getUnformattedText(),
              ChatMessagePacketData.ChatMessageType.fromByte(chatPacket.getType())
            );
        }

        if (packet instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer spawnPlayer = (S0CPacketSpawnPlayer) packet;
            SpawnPlayerPacketData metadata = new SpawnPlayerPacketData();
            metadata.entityId = spawnPlayer.getEntityID();
            metadata.playerId = spawnPlayer.getPlayer();
            metadata.x = spawnPlayer.getX();
            metadata.y = spawnPlayer.getY();
            metadata.z = spawnPlayer.getZ();
            metadata.yaw = spawnPlayer.getYaw();
            metadata.pitch = spawnPlayer.getPitch();
            metadata.currentItem = spawnPlayer.getCurrentItemID();
            metadata.metadata = EntityMetadataFactory.create(spawnPlayer.func_148944_c(), PlayerMetadata.class);
            return metadata;
        }

        return null;
//        return new UnknownPacketData(packet.getClass().getSimpleName());
    }

}
