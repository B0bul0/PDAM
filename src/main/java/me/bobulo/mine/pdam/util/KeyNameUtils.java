package me.bobulo.mine.pdam.util;


import me.bobulo.mine.pdam.shortcut.ShortcutKey;
import org.lwjgl.input.Keyboard;

import java.util.*;

public final class KeyNameUtils {

    private static final Map<Integer, String> SPECIAL_NAMES = new HashMap<>();

    static {
        SPECIAL_NAMES.put(Keyboard.KEY_RETURN, "Enter");
        SPECIAL_NAMES.put(Keyboard.KEY_ESCAPE, "Esc");
        SPECIAL_NAMES.put(Keyboard.KEY_BACK, "Backspace");
        SPECIAL_NAMES.put(Keyboard.KEY_DELETE, "Delete");
        SPECIAL_NAMES.put(Keyboard.KEY_SPACE, "Space");
        SPECIAL_NAMES.put(Keyboard.KEY_TAB, "Tab");
        SPECIAL_NAMES.put(Keyboard.KEY_PRIOR, "PageUp");
        SPECIAL_NAMES.put(Keyboard.KEY_NEXT, "PageDown");
        SPECIAL_NAMES.put(Keyboard.KEY_HOME, "Home");
        SPECIAL_NAMES.put(Keyboard.KEY_END, "End");
        SPECIAL_NAMES.put(Keyboard.KEY_INSERT, "Insert");
        SPECIAL_NAMES.put(Keyboard.KEY_LMETA, "Meta");
        SPECIAL_NAMES.put(Keyboard.KEY_RMETA, "Meta");
        for (int i = 0; i < 12; i++) {
            SPECIAL_NAMES.put(Keyboard.KEY_F1 + i, "F" + (i + 1));
        }
    }

    private KeyNameUtils() {
    }

    public static String format(ShortcutKey key) {
        if (key == null) {
            return "None";
        }

        List<String> mods = new ArrayList<>();
        int[] modifiers = key.getModifiers();
        if (modifiers != null) {
            Set<String> seen = new LinkedHashSet<>();
            for (int m : modifiers) {
                String mn = modifierName(m);
                if (mn != null) seen.add(mn);
            }
            mods.addAll(seen);
        }

        String main = formatKeyCode(key.getKeyCode());

        if (mods.isEmpty()) {
            return main;
        } else {
            String joinedMods = String.join(" + ", mods);
            return joinedMods + " + " + main;
        }
    }

    private static String modifierName(int modifierCode) {
        switch (modifierCode) {
            case Keyboard.KEY_LCONTROL:
            case Keyboard.KEY_RCONTROL:
                return "Ctrl";
            case Keyboard.KEY_LSHIFT:
            case Keyboard.KEY_RSHIFT:
                return "Shift";
            case Keyboard.KEY_LMENU:
            case Keyboard.KEY_RMENU:
                return "Alt";
            case Keyboard.KEY_LMETA:
            case Keyboard.KEY_RMETA:
                return "Meta";
            default:
                return null;
        }
    }

    private static String formatKeyCode(int keyCode) {
        String special = SPECIAL_NAMES.get(keyCode);
        if (special != null) return special;

        try {
            String name = Keyboard.getKeyName(keyCode);
            if (name != null && !name.trim().isEmpty()) {
                return capitalize(name);
            }
        } catch (Exception ignored) {
            // getKeyName can throw if the keyCode is invalid, just ignore and return "Unknown"
        }

        return "Unknown(" + keyCode + ")";
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        s = s.toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}