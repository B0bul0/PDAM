package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.PacketDataBuffer;
import me.bobulo.mine.pdam.feature.packet.data.SerializerKey;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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

    public static class Serializer implements PacketDataSerializer<ClientSettingsClientPacketData> {

        @Override
        public SerializerKey getKey() {
            return new SerializerKey(ConnectionState.PLAY, PacketDirection.CLIENT, 0x15);
        }

        @Override
        public @NotNull ClientSettingsClientPacketData read(@NotNull PacketDataBuffer buf) throws IOException {
            ClientSettingsClientPacketData data = new ClientSettingsClientPacketData();
            data.lang = buf.readString(7);
            data.view = buf.readByte();
            data.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(buf.readByte());
            data.enableColors = buf.readBoolean();
            data.modelPartFlags = buf.readUnsignedByte();
            return data;
        }
    }

}

