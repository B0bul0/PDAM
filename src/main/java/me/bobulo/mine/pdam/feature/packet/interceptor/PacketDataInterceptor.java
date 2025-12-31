package me.bobulo.mine.pdam.feature.packet.interceptor;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.metadata.PacketMetadata;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry.PacketDirection;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

public class PacketDataInterceptor extends ChannelDuplexHandler {

    private static final Logger log = LogManager.getLogger(PacketDataInterceptor.class);
    private static final String HANDLER_NAME = "pdam_packet_interceptor";

    private final PacketMonitorFeatureComponent packetMonitor;

    public PacketDataInterceptor(PacketMonitorFeatureComponent packetMonitor) {
        this.packetMonitor = packetMonitor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet) {
            try {
                handleIncomingPacket((Packet<?>) msg);
            } catch (Exception exception) {
                log.warn("Error handling incoming packet: ", exception);
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Packet) {
            try {
                handleOutgoingPacket((Packet<?>) msg);
            } catch (Exception exception) {
                log.warn("Error handling outgoing packet: ", exception);
            }
        }

        super.write(ctx, msg, promise);
    }

    private void handleIncomingPacket(Packet<?> packet) {
        PacketMetadata packetMetadata = PacketDataMapper.map(packet);
        if (packetMetadata != null) {
            registerIncomingPacket(packetMetadata);
        }
    }

    private void handleOutgoingPacket(Packet<?> packet) {
        // TODO Pacotes enviados
    }

    private void registerIncomingPacket(PacketMetadata packetMetadata) {
        packetMonitor.addPacketEntry(new PacketLogEntry(Instant.now(), packetMetadata, PacketDirection.INCOMING));
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        log.debug("Client connected to server, adding packet interceptor. {}");
        event.manager.channel().pipeline().addBefore("packet_handler", HANDLER_NAME, this);
    }

//    @SubscribeEvent
//    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
//        event.manager.channel().pipeline().remove(HANDLER_NAME);
//    }

}