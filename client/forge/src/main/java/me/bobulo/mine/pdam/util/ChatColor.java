package me.bobulo.mine.pdam.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ChatColor {

    BLACK('0', 0),
    DARK_BLUE('1', 1),
    DARK_GREEN('2', 2),
    DARK_AQUA('3', 3),
    DARK_RED('4', 4),
    DARK_PURPLE('5', 5),
    GOLD('6', 6),
    GRAY('7', 7),
    DARK_GRAY('8', 8),
    BLUE('9', 9),
    GREEN('a', 10),
    AQUA('b', 11),
    RED('c', 12),
    LIGHT_PURPLE('d', 13),
    YELLOW('e', 14),
    WHITE('f', 15),
    OBFUSCATED('k', -1),
    BOLD('l', -1),
    STRIKETHROUGH('m', -1),
    UNDERLINE('n', -1),
    ITALIC('o', -1),
    RESET('r', -1);

    public static final List<ChatColor> ALL = Collections.unmodifiableList(Arrays.asList(values()));

    private static final Map<Integer, ChatColor> COLORS_BY_INDEX = Collections.unmodifiableMap(
      Arrays.stream(values())
        .filter(ChatColor::isColor)
        .collect(Collectors.toMap(ChatColor::getColorIndex, Function.identity()))
    );

    private final char formatChar;
    private final String formatSequence;
    private final int colorIndex;

    ChatColor(char formatChar, int colorIndex) {
        this.formatChar = formatChar;
        this.colorIndex = colorIndex;
        this.formatSequence = "§" + formatChar;
    }

    public char getFormatChar() {
        return formatChar;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public boolean isColor() {
        return colorIndex >= 0;
    }

    public boolean isFormat() {
        return colorIndex < 0 && this != RESET;
    }

    public boolean isReset() {
        return this == RESET;
    }

    @Override
    public String toString() {
        return formatSequence;
    }

    public static ChatColor fromColorIndex(int i) {
        return COLORS_BY_INDEX.get(i);
    }

}
