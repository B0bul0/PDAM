package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public final class ChatMessageServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ChatMessage";

    private IChatComponent message;
    private ChatMessageType type;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public enum ChatMessageType {
        CHAT((byte) 0),
        SYSTEM((byte) 1),
        ACTION_BAR((byte) 2);

        private final byte value;

        ChatMessageType(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

        public static ChatMessageType fromByte(byte b) {
            for (ChatMessageType type : values()) {
                if (type.value == b) {
                    return type;
                }
            }
            return null;
        }
    }

    public static class Extractor implements PacketDataExtractor<ChatMessageServerPacketData, S02PacketChat> {

        @Override
        public @NotNull ChatMessageServerPacketData extract(@NotNull S02PacketChat packet) {
            ChatMessageServerPacketData data = new ChatMessageServerPacketData();

            data.message = packet.getChatComponent();
            data.type = ChatMessageType.fromByte(packet.getType());

            return data;
        }
    }

}
