package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;

import java.util.List;

import static imgui.ImGui.beginMenu;
import static imgui.ImGui.endMenu;

/**
 * Renders a configuration menu for a feature using ImGui.
 */
public final class ConfigMenuImGuiRender extends AbstractFeatureModule implements MenuImGuiRender {

    private List<FeatureConfigImGuiRender> configs;

    @Override
    protected void onInitialize() {
        this.configs = getFeature().getBehaviors(FeatureConfigImGuiRender.class);
    }

    @Override
    public void draw() {
        if (configs == null || configs.isEmpty()) {
            return;
        }

        if (beginMenu(getFeature().getName())) {
            for (FeatureConfigImGuiRender config : configs) {
                config.draw();
            }
            endMenu();
        }
    }

}
