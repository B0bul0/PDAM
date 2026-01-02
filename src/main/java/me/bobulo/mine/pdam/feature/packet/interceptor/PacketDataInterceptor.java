package me.bobulo.mine.pdam.feature.packet.interceptor;

import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.event.ReceivePacketEvent;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.metadata.PacketMetadata;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Instant;

public class PacketDataInterceptor {

    private final PacketMonitorFeatureComponent packetMonitor;

    public PacketDataInterceptor(PacketMonitorFeatureComponent packetMonitor) {
        this.packetMonitor = packetMonitor;
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {
        PacketMetadata packetMetadata = PacketDataMapper.map(event.getPacket());
        if (packetMetadata != null) {
            registerIncomingPacket(packetMetadata);
        }
    }

    private void handleOutgoingPacket(Packet<?> packet) {
        PacketMetadata packetMetadata = PacketDataMapper.map(packet);
        if (packetMetadata != null) {
            registerOutgoingPacket(packetMetadata);
        }
    }

    private void registerIncomingPacket(PacketMetadata packetMetadata) {
        packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetMetadata, PacketDirection.INCOMING));
    }

    private void registerOutgoingPacket(PacketMetadata packetMetadata) {
        packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetMetadata, PacketDirection.OUTGOING));
    }

}