package me.bobulo.mine.pdam.feature.designtools;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import me.bobulo.mine.pdam.feature.imgui.MenuImGuiRender;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static imgui.ImGui.*;

public final class DesignToolsMenuImguiRender extends AbstractFeatureBehavior implements MenuImGuiRender {

    @NotNull
    private List<ToolbarItemWindow> registeredWindows;

    public DesignToolsMenuImguiRender() {
        this.registeredWindows = Collections.emptyList();
    }

    @Override
    protected void onEnable(@NotNull FeatureBehaviorEnableContext context) {
        this.registeredWindows = context.getFeature().getBoundValues(ToolbarItemWindow.class);
    }

    @Override
    protected void onDisable(@NotNull FeatureBehaviorDisableContext context) {
        this.registeredWindows = Collections.emptyList();
    }

    @Override
    public void draw() {
        if (registeredWindows.isEmpty()) {
            return;
        }

        if (beginMenu("Design Tools##DesignToolsMenuImguiRender")) {

            for (ToolbarItemWindow registeredWindow : registeredWindows) {
                if (menuItem(registeredWindow.getMenuName() + "##" + registeredWindow.getClass().getSimpleName(), registeredWindow.isVisible())) {
                    registeredWindow.toggleVisible();
                }
            }

            endMenu();
        }
    }

}
