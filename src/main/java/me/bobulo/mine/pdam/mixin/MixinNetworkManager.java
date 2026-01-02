package me.bobulo.mine.pdam.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.bobulo.mine.pdam.feature.packet.event.ReceivePacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "channelRead0*", at = @At("HEAD"))
    private void onReceivePacket(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ReceivePacketEvent(packet));
    }

}
