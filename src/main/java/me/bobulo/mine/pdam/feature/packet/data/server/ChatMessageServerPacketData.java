package me.bobulo.mine.pdam.feature.packet.data.server;

import org.jetbrains.annotations.NotNull;

public final class ChatMessageServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "ChatMessage";

    private final String plainMessage;
    private final ChatMessageType type;

    public ChatMessageServerPacketData(String plainMessage, ChatMessageType type) {
        this.plainMessage = plainMessage;
        this.type = type;
    }

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public String getPlainMessage() {
        return plainMessage;
    }

    public ChatMessageType getType() {
        return type;
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

}
