package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.jetbrains.annotations.NotNull;

public final class ChatMessageClientPacketData implements ClientPacketData {

    private String message;

    public static class Extractor implements PacketDataExtractor<ChatMessageClientPacketData, C01PacketChatMessage> {

        @Override
        public @NotNull ChatMessageClientPacketData extract(@NotNull C01PacketChatMessage packet) {
            ChatMessageClientPacketData data = new ChatMessageClientPacketData();
            data.message = packet.getMessage();
            return data;
        }

    }

}