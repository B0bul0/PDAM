package me.bobulo.mine.pdam.mixin;

import me.bobulo.mine.pdam.feature.bungeebypass.BungeeBypass;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {

    @Unique
    private static final Logger pDAM$log = LogManager.getLogger(MixinC00Handshake.class);

    @Shadow
    private String ip;
    @Shadow
    private int protocolVersion;
    @Shadow
    private int port;
    @Shadow
    private EnumConnectionState requestedState;

    /**
     * @author B0bul0
     * @reason BungeeCord Spoof IP/UUID Injection
     */
    @Overwrite
    public void writePacketData(PacketBuffer buf) {
        if (BungeeBypass.isFeatureEnabled()) {
            buf.writeVarIntToBuffer(this.protocolVersion);

            String spoofedIp = BungeeBypass.SPOOFED_IP.get();
            String spoofedUuid = BungeeBypass.SPOOFED_UUID.get();
            String injectHost = BungeeBypass.INJECTED_HOST.get();

            String bungeePayload = injectHost + "\00" + spoofedIp + "\00" + spoofedUuid;

            buf.writeString(bungeePayload);

            buf.writeShort(this.port);
            buf.writeVarIntToBuffer(this.requestedState.getId());

            pDAM$log.info("[PDAM] Injected handshake data: Host='{}', Spoofed IP='{}', Spoofed UUID='{}'",
                    injectHost, spoofedIp, spoofedUuid);
            return;
        }

        // Default behavior
        buf.writeVarIntToBuffer(this.protocolVersion);
        buf.writeString(this.ip + "\u0000FML\u0000");
        buf.writeShort(this.port);
        buf.writeVarIntToBuffer(this.requestedState.getId());
    }
}