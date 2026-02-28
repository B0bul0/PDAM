package me.bobulo.mine.pdam.feature.packet.data.server;

import com.mojang.authlib.GameProfile;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.world.WorldSettings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class PlayerListItemServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "PlayerListItem";

    private S38PacketPlayerListItem.Action action;
    private final List<AddPlayerData> players = new ArrayList<>();

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<PlayerListItemServerPacketData, S38PacketPlayerListItem> {

        @Override
        public @NotNull PlayerListItemServerPacketData extract(@NotNull S38PacketPlayerListItem packet) {
            PlayerListItemServerPacketData data = new PlayerListItemServerPacketData();

            data.action = packet.getAction();

            for (S38PacketPlayerListItem.AddPlayerData addPlayerData : packet.getEntries()) {
                data.players.add(new AddPlayerData(addPlayerData));
            }

            return data;
        }

    }

    public static class AddPlayerData {

        private final int ping;
        private final WorldSettings.GameType gamemode;
        private final GameProfile profile;
        private final String displayName;

        public AddPlayerData(S38PacketPlayerListItem.AddPlayerData data) {
            this.ping = data.getPing();
            this.gamemode = data.getGameMode();
            this.profile = data.getProfile();
            this.displayName = data.getDisplayName() != null ? data.getDisplayName().getUnformattedText() : null;
        }

    }
}

