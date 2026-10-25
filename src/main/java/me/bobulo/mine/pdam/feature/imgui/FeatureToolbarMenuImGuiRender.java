package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static imgui.ImGui.menuItem;

/**
 * Renders toolbar item windows in the menu.
 */
public final class FeatureToolbarMenuImGuiRender extends AbstractFeatureBehavior implements MenuImGuiRender {

    private List<ToolbarItemWindow> registeredWindows;

    @Override
    protected void onEnable(@NotNull FeatureBehaviorEnableContext context) {
        this.registeredWindows = context.getFeature().getBoundValues(ToolbarItemWindow.class);
    }

    @Override
    protected void onDisable(@NotNull FeatureBehaviorDisableContext context) {
        this.registeredWindows = null;
    }

    @Override
    public void draw() {
        if (registeredWindows == null || registeredWindows.isEmpty()) {
            return;
        }

        for (ToolbarItemWindow registeredWindow : registeredWindows) {
            if (menuItem(registeredWindow.getMenuName() + "##" + registeredWindow.getClass().getSimpleName(), registeredWindow.isVisible())) {
                registeredWindow.toggleVisible();
            }
        }
    }

}
