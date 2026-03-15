package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import org.jetbrains.annotations.NotNull;

public final class PlayerAbilitiesServerPacketData implements ServerPacketData {

    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    public static class Extractor implements PacketDataExtractor<PlayerAbilitiesServerPacketData, S39PacketPlayerAbilities> {

        @Override
        public @NotNull PlayerAbilitiesServerPacketData extract(@NotNull S39PacketPlayerAbilities packet) {
            PlayerAbilitiesServerPacketData data = new PlayerAbilitiesServerPacketData();
            data.invulnerable = packet.isInvulnerable();
            data.flying = packet.isFlying();
            data.allowFlying = packet.isAllowFlying();
            data.creativeMode = packet.isCreativeMode();
            data.flySpeed = packet.getFlySpeed();
            data.walkSpeed = packet.getWalkSpeed();
            return data;
        }

    }

}