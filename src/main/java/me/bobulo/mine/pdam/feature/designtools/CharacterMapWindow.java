package me.bobulo.mine.pdam.feature.designtools;

import com.google.common.primitives.Chars;
import imgui.ImGuiListClipper;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.mixin.GuiChatInvoker;
import me.bobulo.mine.pdam.util.ChatColor;
import me.bobulo.mine.pdam.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.Validate;

import java.util.function.Predicate;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.MCFontImGui.mcText;

public class CharacterMapWindow extends AbstractRenderItemWindow {

    private static final int MAX_CHAR = 65536;
    private static final int COLUMNS = 14;

    private static final char[] ALL_CHAR_VALUES = new char[MAX_CHAR];
    private static final char[] ALLOWED_CHAT_CHAR_VALUES;
    private static final char[] NUMBERS_CHAR_VALUES = Chars.concat(
      createArray(48, 57),
      createArray(9312, 9371),
      createArray(9451, 9471),
      createArray(10102, 10131)
    );

    private static final char[] ARROWS_CHAR_VALUES = Chars.concat(
      createArray(10136, 10174),
      new char[]{'⟀', '⫷', '⫸', '➔'},
      createArray(10224, 10239),
      createArray(10496, 10623),
      createArray(11008, 11025),
      createArray(11065, 11084)
    );

    private static final char[] BEST_CHAT_CHAR_VALUES = Chars.concat(new char[]{
        '®', 'Θ', '۞', '۩', '܍', '܌', '܀', 'ܫ', '࿄', '࿈', '࿉', '࿊', '࿋', '࿌',
        '࿎', '࿏', '࿒', '྿', '྾', '᠅', '᠈', '᠉', 'ᴥ', '†', '‡', '•', '‣', '⁂',
        '⁑', '∞', '∅', '⌀', '⌚', '⌛', '⌹', '☃', '★', '☆', '☕', '☔', '☠', '☗',
        '☖', '☘', '⭐', '⭑', '⭒', '⭓', '⭔', 'ꕕ', 'ꕔ', 'ꕹ', 'ꖴ', 'ꙮ', '꙰',
        '❍', '❏', '❐', '❑', '❒', '❖', '⟁'
      },
      createArray(9762, 9885),
      createArray(9888, 9905),
      createArray(9920, 9923),
      createArray(9985, 9988),
      createArray(9990, 9993),
      createArray(9996, 10023),
      createArray(10025, 10059),
      createArray(10072, 10078),
      createArray(10082, 10101),
      createArray(11026, 11055),
      createArray(43048, 43051)
    );

    static {
        for (int i = 0; i < MAX_CHAR; i++) {
            ALL_CHAR_VALUES[i] = (char) i;
        }

        ALLOWED_CHAT_CHAR_VALUES = createArray(CharacterMapWindow::isAllowedChatCharacter);
    }

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
        text("Colors preview:");

        for (ChatColor color : ChatColor.ALL) {
            if (!color.isColor()) {
                continue;
            }

            mcText(color.toString() + color.getFormatChar(), 0xFFFFFFFF, false, 2F);
            if (isItemHovered()) {
                setTooltip("Color: " + color.name() + "\n" +
                  "Code: " + color.getFormatChar());
            }

            sameLine();
        }

        sameLine();
        if (button("Copy §")) {
            setClipboardText("§");
        }

        separator();

        if (beginTabBar("CharacterTabs")) {
            if (beginTabItem("All Characters")) {
                if (beginChild("all_chars_scroll", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
                    charactersTable(ALL_CHAR_VALUES);
                    endChild();
                }
                endTabItem();
            }

            if (beginTabItem("Chat Allowed Characters")) {
                if (beginChild("allowed_chars_scroll", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
                    charactersTable(ALLOWED_CHAT_CHAR_VALUES);
                    endChild();
                }
                endTabItem();
            }

            if (beginTabItem("Best Characters")) {
                if (beginChild("best_chars_scroll", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
                    charactersTable(BEST_CHAT_CHAR_VALUES);
                    endChild();
                }
                endTabItem();
            }

            if (beginTabItem("Numbers")) {
                if (beginChild("numbers_chars_scroll", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
                    charactersTable(NUMBERS_CHAR_VALUES);
                    endChild();
                }
                endTabItem();
            }

            if (beginTabItem("Arrows")) {
                if (beginChild("arrows_chars_scroll", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
                    charactersTable(ARROWS_CHAR_VALUES);
                    endChild();
                }
                endTabItem();
            }

            endTabBar();
        }

    }

    private void charactersTable(Predicate<Character> charFilter) {
        char[] charValues = new char[MAX_CHAR];
        int counter = 0;
        for (int i = 0; i < MAX_CHAR; i++) {
            if (charFilter.test((char) i)) {
                charValues[counter++] = (char) i;
            }
        }

        char[] displayCharValues = new char[counter];
        System.arraycopy(charValues, 0, displayCharValues, 0, counter);
        charactersTable(displayCharValues);
    }

    private void charactersTable(char[] charValues) {
        int lines = (int) Math.ceil((double) charValues.length / COLUMNS);

        columns(COLUMNS, "table_chars", false);
        clipper.begin(lines);

        while (clipper.step()) {
            for (int row = clipper.getDisplayStart(); row < clipper.getDisplayEnd(); row++) {
                if (row >= lines) break;

                for (int col = 0; col < COLUMNS; col++) {
                    int i = (row * COLUMNS) + col;
                    if (i >= charValues.length) {
                        nextColumn();
                        continue;
                    }

                    char c = charValues[i];

                    StringBuilder styleText = new StringBuilder();
                    if (selectedColor.get() != 15)
                        styleText.append(ChatColor.fromColorIndex(selectedColor.get()));

                    if (bold.get()) styleText.append(ChatColor.BOLD);
                    if (italic.get()) styleText.append(ChatColor.ITALIC);
                    if (underline.get()) styleText.append(ChatColor.UNDERLINE);
                    if (strikethrough.get()) styleText.append(ChatColor.STRIKETHROUGH);

                    styleText.append(c);

                    String formatedText = styleText.toString();

                    mcText(formatedText, 0xFFFFFFFF, false, 3F);

                    if (beginPopupContextItem("char_popup##" + i, ImGuiPopupFlags.MouseButtonLeft | ImGuiPopupFlags.MouseButtonRight)) {
                        text("Char: " + c);
                        text("ID: " + (int) c);
                        text("Hex: 0x" + Integer.toHexString(c).toUpperCase());

                        separator();
                        text("Copy options:");
                        if (button("Copy char")) {
                            setClipboardText(String.valueOf(c));
                            closeCurrentPopup();
                        }

                        if (button("Copy with formatting")) {
                            setClipboardText(formatedText);
                            closeCurrentPopup();
                        }

                        separator();
                        text("Chat options:");
                        if (button("Send to chat")) {
                            PlayerUtils.sendChatMessage(formatedText.replace("§", "&"));
                            closeCurrentPopup();
                        }

                        if (button("Open in chat input")) {
                            String chatText = formatedText.replace("§", "&");

                            if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
                                GuiChatInvoker guiChat = (GuiChatInvoker) Minecraft.getMinecraft().currentScreen;
                                guiChat.invokeSetText(chatText, false);
                            } else {
                                Minecraft.getMinecraft().displayGuiScreen(new GuiChat(chatText));
                            }

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

    private static boolean isAllowedChatCharacter(char value) {
        return value != 167 && value >= ' ' && value != 127;
    }

    private static char[] createArray(Predicate<Character> filter) {
        char[] temp = new char[MAX_CHAR];
        int count = 0;
        for (int i = 0; i < MAX_CHAR; i++) {
            char c = (char) i;
            if (filter.test(c)) {
                temp[count++] = c;
            }
        }

        char[] result = new char[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    private static char[] createArray(int initChar, int endChar) {
        Validate.isTrue(endChar >= initChar, "endChar must be greater than or equal to initChar");
        int size = endChar - initChar + 1;
        char[] values = new char[size];
        for (int i = 0; i < size; i++) {
            values[i] = (char) (initChar + i);
        }

        return values;
    }

}
