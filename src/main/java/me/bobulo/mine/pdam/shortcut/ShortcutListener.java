package me.bobulo.mine.pdam.shortcut;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShortcutListener {

    private static final Logger log = LogManager.getLogger(ShortcutListener.class);

    private final ShortcutRegistry shortcutRegistry;

    public ShortcutListener(ShortcutRegistry shortcutRegistry) {
        this.shortcutRegistry = shortcutRegistry;
    }

    @SubscribeEvent
    public void onKeyInput(TickEvent.ClientTickEvent event) {
        for (Shortcut shortcut : shortcutRegistry.getShortcuts()) {
            if (shortcut.isJustPressed()) {
                try {
                    Runnable action = shortcut.getAction();
                    if (action != null) {
                        action.run();
                    }
                } catch (Exception e) {
                    log.error("Error executing shortcut '{}'", shortcut.getNameKey(), e);
                }
            }
        }
    }

}
