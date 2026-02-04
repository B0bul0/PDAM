package me.bobulo.mine.pdam.feature.designtools;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;

import java.util.List;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;
import static me.bobulo.mine.pdam.imgui.util.MCFontImGui.mcText;

/**
 * A GUI window for formatting and previewing chat messages with color codes.
 */
public class MessageFormatterWindow extends AbstractRenderItemWindow {

    private final ImString message = new ImString(1000);

    // options
    private final ImFloat scale = new ImFloat(1.3f); // message scale
    private final ImBoolean allowOtherColorCodes = new ImBoolean(true); // enable & formatting codes
    private final ImBoolean wrapText = new ImBoolean(true); // wrap text in preview
    private final ImInt wrapWidth = new ImInt(320); // wrap width in pixels

    public MessageFormatterWindow() {
        super("Message Formatter");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(400, 450, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Chat Message Formatter##MessageFormatterWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        if (beginPopup("OptionsPopup")) {
            text("Text Options");
            separator();
            inputFloat("Text Scale", scale, 0.1f, 0.5f, "%.1f");
            checkbox("Allow & Color Codes", allowOtherColorCodes);

            checkbox("Wrap Text in Preview", wrapText);
            if (!wrapText.get()) {
                beginDisabled();
            }

            inputInt("Wrap Width (px)", wrapWidth, 10, 30, ImGuiInputTextFlags.EnterReturnsTrue);
            if (wrapWidth.get() < 40) {
                wrapWidth.set(40);
            }

            if (!wrapText.get()) {
                endDisabled();
            }

            endPopup();
        }

        if (button("Options")) {
            openPopup("OptionsPopup");
        }

        sameLine();

        int messageLength = message.getLength();
        text("Length: " + messageLength);

        float availableHeight = getContentRegionAvailY();
        inputTextMultiline("##ChatMessageInput", message, -1, availableHeight * 0.4f);

        spacing();
        spacing();
        text("Preview:");

        if (beginChild("MessagePreviewRegion", 0, 0, true)) {
            String text = message.get().trim();

            for (String line : text.split("\n")) {
                String formattedMessage = getFormattedMessage(line);

                if (wrapText.get()) {
                    List<String> strings = Minecraft.getMinecraft().fontRendererObj
                      .listFormattedStringToWidth(formattedMessage, wrapWidth.get());

                    for (String string : strings) {
                        mcText(string, scale.get());
                    }

                    continue;
                }

                mcText(formattedMessage, scale.get());
            }

        }
        endChild();
    }

    private String getFormattedMessage(String message) {
        if (allowOtherColorCodes.get()) {
            return message.replace("&", "§");
        }

        return message;
    }

}
