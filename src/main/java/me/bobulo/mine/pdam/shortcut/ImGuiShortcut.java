package me.bobulo.mine.pdam.shortcut;

import me.bobulo.mine.pdam.util.KeyNameUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;

public class ImGuiShortcut {

    private final Shortcut shortcut;
    private boolean editing = false;

    public ImGuiShortcut(Shortcut shortcut) {
        this.shortcut = shortcut;
    }

    public void render() {
        ShortcutKey key = shortcut.getKey();

        String keyName = KeyNameUtils.format(key);
        text("Shortcut");
        sameLine();

        if (editing) {
            button("Press key (Esc to cancel)...");

            for (int pressedKey = 1; pressedKey < 256; pressedKey++) {
                if (Keyboard.isKeyDown(pressedKey)) {

                    if (pressedKey == Keyboard.KEY_ESCAPE) {
                        editing = false;
                        break;
                    }

                    if (pressedKey == Keyboard.KEY_LCONTROL || pressedKey == Keyboard.KEY_RCONTROL ||
                      pressedKey == Keyboard.KEY_LSHIFT || pressedKey == Keyboard.KEY_RSHIFT ||
                      pressedKey == Keyboard.KEY_LMENU || pressedKey == Keyboard.KEY_RMENU) {
                        continue;
                    }

                    List<Integer> activeMods = new ArrayList<>();
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
                        activeMods.add(Keyboard.KEY_LCONTROL);
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
                        activeMods.add(Keyboard.KEY_LSHIFT);
                    if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))
                        activeMods.add(Keyboard.KEY_LMENU);

                    int[] modsArray = new int[activeMods.size()];
                    for (int i = 0; i < activeMods.size(); i++) modsArray[i] = activeMods.get(i);

                    shortcut.setKey(new ShortcutKey(pressedKey, modsArray));
                    editing = false;
                    break;
                }
            }
        } else {
            if (button(keyName + "##editShortcut_" + shortcut.getNameKey())) {
                editing = true;
            }
        }

        sameLine();
        text(shortcut.getNameKey());

        sameLine();
        if (button("Clear##clearShortcut_" + shortcut.getNameKey())) {
            shortcut.setKey(null);
        }
    }

}
