package me.bobulo.mine.pdam.shortcut;

/**
 * Represents a keyboard shortcut, consisting of a key code and optional modifier keys.
 * Provides methods for serialization and deserialization to a string format.
 */
public final class ShortcutKey {

    public static ShortcutKey of(int keyCode, int... modifiers) {
        return new ShortcutKey(keyCode, modifiers);
    }

    public static ShortcutKey serialize(String serialized) {
        String[] parts = serialized.split(":");
        int keyCode = Integer.parseInt(parts[0]);
        int[] modifiers = new int[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
            modifiers[i - 1] = Integer.parseInt(parts[i]);
        }
        return new ShortcutKey(keyCode, modifiers);
    }

    private final int keyCode;
    private final int[] modifiers; // optional modifiers (e.g., Ctrl, Shift, Alt)

    ShortcutKey(int keyCode, int[] modifiers) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int[] getModifiers() {
        return modifiers;
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(keyCode);
        if (modifiers != null) {
            for (int m : modifiers) {
                sb.append(":").append(m);
            }
        }
        return sb.toString();
    }

}
