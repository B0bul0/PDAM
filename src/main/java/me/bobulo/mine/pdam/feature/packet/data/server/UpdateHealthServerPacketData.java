package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import org.jetbrains.annotations.NotNull;

public final class UpdateHealthServerPacketData implements ServerPacketData {

    private float health;
    private int foodLevel;
    private float saturationLevel;

    public static class Extractor implements PacketDataExtractor<UpdateHealthServerPacketData, S06PacketUpdateHealth> {

        @Override
        public @NotNull UpdateHealthServerPacketData extract(@NotNull S06PacketUpdateHealth packet) {
            UpdateHealthServerPacketData data = new UpdateHealthServerPacketData();
            data.health = packet.getHealth();
            data.foodLevel = packet.getFoodLevel();
            data.saturationLevel = packet.getSaturationLevel();
            return data;
        }

    }

}