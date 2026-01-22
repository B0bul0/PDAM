package me.bobulo.mine.pdam.feature.designtools;

import me.bobulo.mine.pdam.feature.imgui.MenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.ImGuiListenerFeatureModule;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static imgui.ImGui.*;

public final class DesignToolsMenuImguiRender extends AbstractFeatureModule implements MenuImGuiRender {

    @NotNull
    private final DesignToolsContext context;

    @NotNull
    private List<ToolbarItemWindow> registeredWindows;

    public DesignToolsMenuImguiRender(@NotNull DesignToolsContext context) {
        this.context = context;
        this.registeredWindows = Collections.emptyList();
    }

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
        this.registeredWindows = Collections.emptyList();
    }

    @Override
    public void draw() {
        if (beginMenu("Design Tools##DesignToolsMenuImguiRender")) {

            boolean enabled = DesignToolsContext.ENABLED.get();
            if (checkbox("Design Tools", enabled)) {
                DesignToolsContext.ENABLED.set(!enabled);
            }

            if (context.getFeature().isEnabled()) {
                separator();

                for (ToolbarItemWindow registeredWindow : registeredWindows) {
                    if (menuItem(registeredWindow.getMenuName() + "##" + registeredWindow.getClass().getSimpleName(), registeredWindow.isVisible())) {
                        registeredWindow.toggleVisible();
                    }
                }
            }

            endMenu();
        }
    }

}
