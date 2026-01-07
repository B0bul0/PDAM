package me.bobulo.mine.pdam.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.event.DecompressPacketEvent;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(NettyCompressionDecoder.class)
public class MixinNettyCompressionDecoder {

    @Inject(method = "decode", at = @At("RETURN"))
    private void onDecodeReturn(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, CallbackInfo ci) {
        if (out.isEmpty()) {
            return;
        }

        Object lastElement = out.get(out.size() - 1);

        if (lastElement instanceof ByteBuf) {
            ByteBuf decompressedBuf = (ByteBuf) lastElement;
            int readerIndex = decompressedBuf.readerIndex();

            try {
                if (decompressedBuf.readableBytes() > 0) {
                    EnumConnectionState nmsState = ctx.channel().attr(NetworkManager.attrKeyConnectionState).get();
                    ConnectionState state = ConnectionState.fromNMS(nmsState);
                    if (state == null) {
                        return;
                    }

                    int packetId = pdam$readVarInt(decompressedBuf);
                    MinecraftForge.EVENT_BUS.post(new DecompressPacketEvent(state, packetId, decompressedBuf.slice()));
                }
            } finally {
                decompressedBuf.readerIndex(readerIndex);
            }
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