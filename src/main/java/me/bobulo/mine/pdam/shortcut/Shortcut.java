package me.bobulo.mine.pdam.shortcut;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import org.lwjgl.input.Keyboard;

public final class Shortcut {

    public static Shortcut of(String nameKey, String configKey, Runnable action) {
        return new Shortcut(nameKey, configKey, action, null);
    }

    public static Shortcut of(String nameKey, String configKey, Runnable action, ShortcutKey defaultKey) {
        return new Shortcut(nameKey, configKey, action, defaultKey);
    }

    private final String nameKey;
    private final String configKey;
    private final Runnable action;

    private final ConfigValue<String> config;

    private ShortcutKey key;

    private boolean lastState = false;
    private boolean ignoring = false; // To prevent immediate re-triggering after changing the key

    private Shortcut(String nameKey, String configKey, Runnable action, ShortcutKey key) {
        this.nameKey = nameKey;
        this.configKey = configKey;
        this.action = action;
        this.key = key;

        this.config = ConfigProperty.of(configKey, String.class, () -> key == null ? null : key.serialize());
        this.key = config.get() == null || config.get().isEmpty() ? null : ShortcutKey.serialize(config.get());
    }

    public String getNameKey() {
        return nameKey;
    }

    public Runnable getAction() {
        return action;
    }

    public ShortcutKey getKey() {
        return key;
    }

    public void setKey(ShortcutKey key) {
        this.key = key;

        if (key == null) {
            this.config.set(null);
        } else {
            this.config.set(key.serialize());
        }
        this.ignoring = true;
    }

    public Shortcut register() {
        ShortcutRegistry.getInstance().registerShortcut(this);
        return this;
    }

    public boolean isJustPressed() {
        if (key == null) {
            return false;
        }

        if (ignoring) {
            if (!Keyboard.isKeyDown(key.getKeyCode())) {
                ignoring = false;
            }
            return false;
        }

        if (!Keyboard.isKeyDown(key.getKeyCode())) {
            lastState = false;
            return false;
        }

        if (key.getModifiers() != null) {
            for (int modifier : key.getModifiers()) {
                boolean modPressed;

                if (modifier == Keyboard.KEY_LSHIFT || modifier == Keyboard.KEY_RSHIFT) {
                    modPressed = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                } else if (modifier == Keyboard.KEY_LCONTROL || modifier == Keyboard.KEY_RCONTROL) {
                    modPressed = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
                } else if (modifier == Keyboard.KEY_LMENU || modifier == Keyboard.KEY_RMENU) {
                    modPressed = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
                } else {
                    modPressed = Keyboard.isKeyDown(modifier);
                }

                if (!modPressed) {
                    lastState = false;
                    return false;
                }
            }
        }

        if (!lastState) {
            lastState = true;
            return true;
        }

        return false;
    }

}
