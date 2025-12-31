package me.bobulo.mine.pdam.feature.packet.metadata;

public final class ChatMessagePacketMetadata implements PacketMetadata {

    private final String plainMessage;
    private final ChatMessageType type;

    public ChatMessagePacketMetadata(String plainMessage, ChatMessageType type) {
        this.plainMessage = plainMessage;
        this.type = type;
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
