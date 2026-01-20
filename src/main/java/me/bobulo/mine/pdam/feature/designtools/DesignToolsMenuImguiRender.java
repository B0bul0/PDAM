package me.bobulo.mine.pdam.feature.designtools;

import me.bobulo.mine.pdam.feature.imgui.MenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.ImGuiListenerFeatureModule;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;

import java.util.List;
import java.util.stream.Collectors;

import static imgui.ImGui.*;

public class DesignToolsMenuImguiRender extends AbstractFeatureModule implements MenuImGuiRender {

    private List<ToolbarItemWindow> registeredWindows;

    @Override
    protected void onEnable() {
        this.registeredWindows = getFeature().getBehaviors(ImGuiListenerFeatureModule.class)
          .stream()
          .map(ImGuiListenerFeatureModule::getListeners)
          .flatMap(List::stream)
          .filter(ToolbarItemWindow.class::isInstance)
          .map(ToolbarItemWindow.class::cast)
          .collect(Collectors.toList());
    }

    @Override
    protected void onDisable() {
        this.registeredWindows = null;
    }

    @Override
    public void draw() {
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
