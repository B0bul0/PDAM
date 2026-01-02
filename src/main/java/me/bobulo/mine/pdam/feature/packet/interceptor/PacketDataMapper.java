package me.bobulo.mine.pdam.feature.packet.interceptor;

import me.bobulo.mine.pdam.feature.packet.metadata.PacketMetadata;
import me.bobulo.mine.pdam.feature.packet.metadata.ChatMessagePacketMetadata;
import me.bobulo.mine.pdam.feature.packet.metadata.SpawnPlayerPacketMetadata;
import me.bobulo.mine.pdam.feature.packet.metadata.UnknownPacketMetadata;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public final class PacketDataMapper {

    public static PacketMetadata map(Packet<?> packet) {
        if (packet instanceof S02PacketChat) {
            S02PacketChat chatPacket = (S02PacketChat) packet;
            return new ChatMessagePacketMetadata(
              chatPacket.getChatComponent().getUnformattedText(),
              ChatMessagePacketMetadata.ChatMessageType.fromByte(chatPacket.getType())
            );
        }

        if (packet instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer spawnPlayer = (S0CPacketSpawnPlayer) packet;
            SpawnPlayerPacketMetadata metadata = new SpawnPlayerPacketMetadata();
            metadata.entityId = spawnPlayer.getEntityID();
            metadata.playerId = spawnPlayer.getPlayer();
            metadata.x = spawnPlayer.getX();
            metadata.y = spawnPlayer.getY();
            metadata.z = spawnPlayer.getZ();
            metadata.yaw = spawnPlayer.getYaw();
            metadata.pitch = spawnPlayer.getPitch();
            metadata.currentItem = spawnPlayer.getCurrentItemID();
            return metadata;
        }

        return new UnknownPacketMetadata(packet.getClass().getSimpleName());
    }

}
