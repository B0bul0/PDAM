package me.bobulo.mine.pdam.feature.packet.interceptor;

import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.event.ReceivePacketEvent;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketData;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

public class PacketDataInterceptor {

    private static final Logger log = LogManager.getLogger(PacketDataInterceptor.class);
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
        try {
            packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetData, PacketDirection.SERVER));
        } catch (Exception exception) {
            log.warn("Failed to register incoming packet: {}", packetData.getClass().getSimpleName(), exception);
        }
    }

    private void registerOutgoingPacket(PacketData packetData) {
        try {
            packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetData, PacketDirection.CLIENT));
        } catch (Exception exception) {
            log.warn("Failed to register outgoing packet: {}", packetData.getClass().getSimpleName(), exception);
        }
    }

}