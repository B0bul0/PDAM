package me.bobulo.mine.pdam.feature.chat;

import imgui.ImGuiListClipper;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.MCFontImGui.mcText;

public class CharacterMapWindow extends AbstractRenderItemWindow {

    private static final int MAX_CHAR = 65536;
    private static final int COLUMNS = 14;

    private final ImBoolean bold = new ImBoolean(false);
    private final ImBoolean italic = new ImBoolean(false);
    private final ImBoolean underline = new ImBoolean(false);
    private final ImBoolean strikethrough = new ImBoolean(false);

    private final ImInt selectedColor = new ImInt(15);

    private final ImGuiListClipper clipper = new ImGuiListClipper();

    public CharacterMapWindow() {
        super("Character Map");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(700, 600, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Character Map###CharacterMapWindow", isVisible)) {
            renderContent();
        }

        end();
    }

    private void renderContent() {
        text("Color:");

        int splitCount = 8;
        for (EnumChatFormatting chatFormatting : EnumChatFormatting.values()) {
            if (!chatFormatting.isColor()) {
                continue;
            }

            int colorIndex = chatFormatting.getColorIndex();
            radioButton(chatFormatting.name() + "##color" + colorIndex, selectedColor, colorIndex);

            if ((colorIndex + 1) % splitCount != 0) {
                sameLine();
            }
        }

        separator();

        text("Style:");
        checkbox("Bold", bold);
        sameLine();
        checkbox("Italic", italic);
        sameLine();
        checkbox("Underline", underline);
        sameLine();
        checkbox("Strikethrough", strikethrough);

        separator();

        if (beginTabBar("CharacterTabs")) {
            if (beginTabItem("All Characters")) {
                if (beginChild("all_chars_scroll", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
                    allCharacters();
                    endChild();
                }
                endTabItem();
            }

            endTabBar();
        }

    }

    private void allCharacters() {
        int lines = (int) Math.ceil((double) MAX_CHAR / COLUMNS);

        columns(COLUMNS, "table_chars", false);
        clipper.begin(lines);

        while (clipper.step()) {
            for (int row = clipper.getDisplayStart(); row < clipper.getDisplayEnd(); row++) {
                if (row >= lines) break;

                for (int col = 0; col < COLUMNS; col++) {
                    int i = (row * COLUMNS) + col;
                    if (i >= MAX_CHAR) {
                        nextColumn();
                        continue;
                    }

                    char c = (char) i;

                    StringBuilder styleText = new StringBuilder();
                    styleText.append(EnumChatFormatting.func_175744_a(selectedColor.get()));

                    if (bold.get()) styleText.append(EnumChatFormatting.BOLD);
                    if (italic.get()) styleText.append(EnumChatFormatting.ITALIC);
                    if (underline.get()) styleText.append(EnumChatFormatting.UNDERLINE);
                    if (strikethrough.get()) styleText.append(EnumChatFormatting.STRIKETHROUGH);

                    styleText.append(c);

                    String formatedText = styleText.toString();

                    mcText(formatedText, 0xFFFFFFFF, false, 3F);

                    if (beginPopupContextItem("char_popup##" + i, ImGuiPopupFlags.MouseButtonLeft | ImGuiPopupFlags.MouseButtonRight)) {
                        text("Char: " + c);
                        text("ID: " + i);
                        text("Hex: 0x" + Integer.toHexString(i).toUpperCase());
                        separator();

                        if (button("Copy")) {
                            setClipboardText(String.valueOf(c));
                            closeCurrentPopup();
                        }

                        if (button("Send to chat")) {
                            PlayerUtils.sendChatMessage(formatedText.replace("§", "&"));
                            closeCurrentPopup();
                        }

                        if (button("Open in chat input")) {
//                                if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
//                                    GuiChat guiChat = (GuiChat) Minecraft.getMinecraft().currentScreen;
//                                    guiChat.setText(c, false);
//                                }

                            Minecraft.getMinecraft().displayGuiScreen(new GuiChat(formatedText.replace("§", "&")));
                            closeCurrentPopup();
                        }

                        endPopup();
                    }

                    if (isItemHovered()) {
                        setTooltip(
                          "Char: " + c + "\n" +
                            "ID: " + i + "\n" +
                            "Hex: 0x" + Integer.toHexString(i).toUpperCase()
                        );
                    }

                    nextColumn();
                }
            }
        }

        columns(1);
        clipper.end();
    }

}