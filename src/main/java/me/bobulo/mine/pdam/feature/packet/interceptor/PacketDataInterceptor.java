package me.bobulo.mine.pdam.feature.packet.interceptor;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.data.PacketCodecRegistry;
import me.bobulo.mine.pdam.feature.packet.data.PacketData;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.event.CompressPacketEvent;
import me.bobulo.mine.pdam.feature.packet.event.DecompressPacketEvent;
import me.bobulo.mine.pdam.feature.packet.event.ReceivePacketEvent;
import me.bobulo.mine.pdam.feature.packet.event.SendPacketEvent;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Instant;

public class PacketDataInterceptor {

    private static final Logger log = LogManager.getLogger(PacketDataInterceptor.class);
    private final PacketMonitorFeatureComponent packetMonitor;

    public PacketDataInterceptor(PacketMonitorFeatureComponent packetMonitor) {
        this.packetMonitor = packetMonitor;
    }

    /* Minecraft Packet */

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {
        Packet<?> packet = event.getPacket();

        try {
            PacketData packetData = PacketCodecRegistry.extract(packet);
            if (packetData == null) {
                return;
            }

            registerIncomingPacket(packetData);
        } catch (IOException e) {
            log.warn("Failed to extract packet data for incoming packet: {}",
              packet.getClass().getSimpleName(), e);
        }
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {
        Packet<?> packet = event.getPacket();

        try {
            PacketData packetData = PacketCodecRegistry.extract(packet);
            if (packetData == null) {
                return;
            }

            registerOutgoingPacket(packetData);
        } catch (IOException e) {
            log.warn("Failed to extract packet data for outgoing packet: {}",
              packet.getClass().getSimpleName(), e);
        }
    }

    /* Decompressed/Compress Packet */

    @SubscribeEvent
    public void onDecompressPacket(DecompressPacketEvent event) {
        try {
            PacketDataBuffer buf = new PacketDataBuffer(event.getBuffer().duplicate());
            PacketData packetData = PacketCodecRegistry.decode(event.getConnectionState(), PacketDirection.SERVER, event.getPacketId(), buf);

            if (packetData == null) {
                return;
            }

            registerIncomingPacket(packetData);
        } catch (IOException e) {
            log.warn("Failed to decode packet data for packet ID: 0x{}",
              Integer.toHexString(event.getPacketId()), e);
        }
    }

    @SubscribeEvent
    public void onCompressPacket(CompressPacketEvent event) {
        try {
            PacketDataBuffer buf = new PacketDataBuffer(event.getBuffer().duplicate());
            PacketData packetData = PacketCodecRegistry.decode(event.getConnectionState(), PacketDirection.CLIENT, event.getPacketId(), buf);

            if (packetData == null) {
                return;
            }

            registerOutgoingPacket(packetData);
        } catch (IOException e) {
            log.warn("Failed to encode packet data for packet ID: 0x{}",
              Integer.toHexString(event.getPacketId()), e);
        }
    }

    /* Packet Data Registration */

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