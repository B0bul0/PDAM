package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;

import java.util.List;

import static imgui.ImGui.menuItem;

/**
 * Renders toolbar item windows in the menu.
 */
public final class ToolbarMenuImGuiRenderer extends AbstractFeatureModule implements MenuImGuiRender {

    private final List<ToolbarItemWindow> registeredWindows;

    public ToolbarMenuImGuiRenderer(List<ToolbarItemWindow> registeredWindows) {
        this.registeredWindows = registeredWindows;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

    @Override
    public void draw() {
        for (ToolbarItemWindow registeredWindow : registeredWindows) {
            if (menuItem(registeredWindow.getMenuName() + "##" + registeredWindow.getClass().getSimpleName(), registeredWindow.isVisible())) {
                registeredWindow.toggleVisible();
            }
        }
    }

}
