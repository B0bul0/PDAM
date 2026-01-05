package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import org.jetbrains.annotations.NotNull;

public final class PlayerAbilitiesClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "PlayerAbilities";

    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<PlayerAbilitiesClientPacketData, C13PacketPlayerAbilities> {

        @Override
        public @NotNull PlayerAbilitiesClientPacketData extract(@NotNull C13PacketPlayerAbilities packet) {
            PlayerAbilitiesClientPacketData data = new PlayerAbilitiesClientPacketData();
            data.invulnerable = packet.isInvulnerable();
            data.flying = packet.isFlying();
            data.allowFlying = packet.isAllowFlying();
            data.creativeMode = packet.isCreativeMode();
            data.flySpeed = ReflectionUtils.getFieldValue(packet, "flySpeed");
            data.walkSpeed = ReflectionUtils.getFieldValue(packet, "walkSpeed");
            return data;
        }
    }

}

