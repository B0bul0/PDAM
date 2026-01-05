package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import org.jetbrains.annotations.NotNull;

public final class CombatEventServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "CombatEvent";

    private S42PacketCombatEvent.Event eventType;
    private int playerId;
    private int entityId;
    private int duration;
    private String deathMessage;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<CombatEventServerPacketData, S42PacketCombatEvent> {

        @Override
        public @NotNull CombatEventServerPacketData extract(@NotNull S42PacketCombatEvent packet) {
            CombatEventServerPacketData data = new CombatEventServerPacketData();
            data.eventType = packet.eventType;
            data.playerId = packet.field_179774_b;
            data.entityId = packet.field_179775_c;
            data.duration = packet.field_179772_d;
            data.deathMessage = packet.deathMessage;
            return data;
        }

    }
}

