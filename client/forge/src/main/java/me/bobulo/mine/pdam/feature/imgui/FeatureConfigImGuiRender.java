package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.module.FeatureModule;

/**
 * Represents a feature module that can render ImGui configuration menus.
 */
public interface FeatureConfigImGuiRender extends FeatureModule {

    /**
     * Draws the ImGui configuration menu for the feature.
     */
    void draw();

}
