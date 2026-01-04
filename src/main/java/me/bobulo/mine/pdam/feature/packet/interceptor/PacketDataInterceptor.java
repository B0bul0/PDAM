package me.bobulo.mine.pdam.feature.packet.interceptor;

import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.event.ReceivePacketEvent;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketData;
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
        PacketData packetData = PacketDataMapper.map(event.getPacket());
        if (packetData != null) {
            registerIncomingPacket(packetData);
        }
    }

    private void handleOutgoingPacket(Packet<?> packet) {
        PacketData packetData = PacketDataMapper.map(packet);
        if (packetData != null) {
            registerOutgoingPacket(packetData);
        }
    }

    private void registerIncomingPacket(PacketData packetData) {
        packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetData, PacketDirection.INCOMING));
    }

    private void registerOutgoingPacket(PacketData packetData) {
        packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetData, PacketDirection.OUTGOING));
    }

}