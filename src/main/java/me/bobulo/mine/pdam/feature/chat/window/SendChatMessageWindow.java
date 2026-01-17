package me.bobulo.mine.pdam.feature.chat.window;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public class SendChatMessageWindow extends AbstractRenderItemWindow {

    private static final int MINECRAFT_MAX_CHAT_MESSAGE_LENGTH = 256;

    private final ImString message = new ImString(1000);

    // options
    private final ImBoolean sendOnEnter = new ImBoolean(false);
    private final ImBoolean clearAfterSend = new ImBoolean(true);
    private final ImBoolean multiMessage = new ImBoolean(false);

    public SendChatMessageWindow() {
        super("Send Chat Message");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(400, 450, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Send Chat Message###SendChatMessageWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        EntityPlayerSP player = getPlayer();

        boolean sendMessage = false;
        int messageLength = message.getLength();

        text("Length: " + messageLength + " / " + MINECRAFT_MAX_CHAT_MESSAGE_LENGTH);
        if (messageLength > MINECRAFT_MAX_CHAT_MESSAGE_LENGTH) {
            sameLine();
            text("Over minecraft vanilla limit.");
        }

        float footerHeight = getFrameHeightWithSpacing();

        if (sendOnEnter.get()) {
            if (inputTextMultiline("##ChatMessageInput", message, -1, -footerHeight,
              ImGuiInputTextFlags.EnterReturnsTrue | ImGuiInputTextFlags.CtrlEnterForNewLine)) {
                sendMessage = true;
            }
        } else {
            inputTextMultiline("##ChatMessageInput", message, -1, -footerHeight);
        }

        if (button("Send Message")) {
            sendMessage = true;
        }

        sameLine();

        if (beginPopup("OptionsPopup")) {
            checkbox("Send on Enter", sendOnEnter);
            checkbox("Clear After Send", clearAfterSend);
            checkbox("Multi Message (Split by new lines)", multiMessage);
            endPopup();
        }

        if (button("Options")) {
            openPopup("OptionsPopup");
        }

        if (sendMessage && player != null) {
            if (multiMessage.get()) {
                String[] messages = message.get().split("\\r?\\n");
                for (String msg : messages) {
                    String messageText = msg.trim();
                    player.sendChatMessage(messageText);
                }
            } else {
                String messageText = message.get().trim()
                  .replace("\n", " ")
                  .replace("\r", " ");

                player.sendChatMessage(messageText);
            }

            if (clearAfterSend.get()) {
                message.set(""); // Clear the input field after sending
            }

            // Refocus the input field
            setKeyboardFocusHere(-1);
        }

    }

    private EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

}
