package me.bobulo.mine.pdam.shortcut;

import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShortcutRegistry {

    private static final ShortcutRegistry INSTANCE = new ShortcutRegistry();

    public static ShortcutRegistry getInstance() {
        return INSTANCE;
    }

    private final List<Shortcut> shortcuts = new ArrayList<>();

    public ShortcutRegistry() {
        MinecraftForge.EVENT_BUS.register(new ShortcutListener(this));
    }

    public void registerShortcut(@NotNull Shortcut shortcut) {
        shortcuts.add(shortcut);
    }

    public void unregisterShortcut(@NotNull Shortcut shortcut) {
        shortcuts.removeIf(s -> s.equals(shortcut));
    }

    public Shortcut getShortcut(ShortcutKey key) {
        for (Shortcut shortcut : shortcuts) {
            if (shortcut.getKey().equals(key)) {
                return shortcut;
            }
        }
        return null;
    }

    @NotNull
    public Collection<Shortcut> getShortcuts() {
        return shortcuts;
    }

}
