package me.bobulo.mine.pdam.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.event.CompressPacketEvent;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NettyCompressionEncoder.class)
public class MixinNettyCompressionEncoder {

    @Inject(method = "encode*", at = @At("HEAD"))
    private void onEncodeHead(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out, CallbackInfo ci) {
        int readerIndex = msg.readerIndex();


        try {
            if (msg.readableBytes() > 0) {
                EnumConnectionState nmsState = ctx.channel().attr(NetworkManager.attrKeyConnectionState).get();
                ConnectionState state = ConnectionState.fromNMS(nmsState);

                if (state == null) {
                    return;
                }

                int packetId = pdam$readVarInt(msg);
                MinecraftForge.EVENT_BUS.post(new CompressPacketEvent(state, packetId, msg.slice()));
            }
        } finally {
            msg.readerIndex(readerIndex);
        }
    }

    @Unique
    private int pdam$readVarInt(ByteBuf buf) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            if (!buf.isReadable()) {
                break;
            }

            currentByte = buf.readByte();
            value |= (currentByte & 0x7F) << (position * 7);

            if ((currentByte & 0x80) == 0) {
                break;
            }

            position++;
            if (position >= 5) {
                throw new RuntimeException("VarInt too big");
            }
        }

        return value;
    }
}