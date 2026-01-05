package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import org.jetbrains.annotations.NotNull;

public final class ClientSettingsClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "ClientSettings";

    private String lang;
    private int view;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean enableColors;
    private int modelPartFlags;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<ClientSettingsClientPacketData, C15PacketClientSettings> {

        @Override
        public @NotNull ClientSettingsClientPacketData extract(@NotNull C15PacketClientSettings packet) {
            ClientSettingsClientPacketData data = new ClientSettingsClientPacketData();
            data.lang = packet.getLang();
            data.view = ReflectionUtils.getFieldValue(packet, "view");
            data.chatVisibility = packet.getChatVisibility();
            data.enableColors = packet.isColorsEnabled();
            data.modelPartFlags = packet.getModelPartFlags();
            return data;
        }
    }

}

