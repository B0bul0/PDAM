package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S3EPacketTeams;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class TeamsServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "Teams";

    private String teamName;
    private String teamDisplayName;
    private String teamPrefix;
    private String teamSuffix;
    private String nameTagVisibility;
    private int color;
    private Collection<String> players;
    private int action;
    private int friendlyFlags;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<TeamsServerPacketData, S3EPacketTeams> {

        @Override
        public @NotNull TeamsServerPacketData extract(@NotNull S3EPacketTeams packet) {
            TeamsServerPacketData data = new TeamsServerPacketData();
            data.teamName = packet.getName();
            data.teamDisplayName = packet.getDisplayName();
            data.teamPrefix = packet.getPrefix();
            data.teamSuffix = packet.getSuffix();
            data.nameTagVisibility = packet.getNameTagVisibility();
            data.color = packet.getColor();
            data.players = packet.getPlayers();
            data.action = packet.getAction();
            data.friendlyFlags = packet.getFriendlyFlags();
            return data;
        }

    }

}

