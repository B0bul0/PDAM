package me.bobulo.mine.pdam.imgui.toolbar;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;

public class ImGuiToolbar implements ImGuiRenderable {

    private final List<ToolbarItemWindow> registeredWindows = new ArrayList<>();

    /* Register */

    public void registerWindow(@NotNull ToolbarItemWindow window) {
        registeredWindows.add(window);
    }

    public void unregisterWindow(@NotNull ToolbarItemWindow window) {
        registeredWindows.remove(window);
    }

    @Override
    public void newFrame() {
        int windowFlags = ImGuiWindowFlags.NoTitleBar
          | ImGuiWindowFlags.NoResize
          | ImGuiWindowFlags.NoMove
          | ImGuiWindowFlags.NoCollapse
          | ImGuiWindowFlags.NoScrollbar
          | ImGuiWindowFlags.NoScrollWithMouse
          | ImGuiWindowFlags.NoBringToFrontOnFocus
          | ImGuiWindowFlags.NoBackground
          | ImGuiWindowFlags.MenuBar;

        setNextWindowPos(0, 0, ImGuiCond.Always);
        setNextWindowSize(45, 40, ImGuiCond.Always);

        if (begin("##Toolbar", windowFlags)) {
            if (beginMenuBar()) {

                if (beginMenu("PDAM")) {

                    for (ToolbarItemWindow registeredWindow : registeredWindows) {
                        if (menuItem(registeredWindow.getMenuName() + "##" + registeredWindow.getClass().getSimpleName(), registeredWindow.isVisible())) {
                            registeredWindow.toggleVisible();
                        }
                    }

                    endMenu();
                }

                endMenuBar();
            }
            end();
        }
    }

}
