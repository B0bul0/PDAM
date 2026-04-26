package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;

import static imgui.ImGui.menuItem;

/**
 * Renders a menu item in the ImGui menu to toggle the enabled state of a feature.
 */
public final class ToggleEnabledMenuImGuiRender extends AbstractFeatureModule implements MenuImGuiRender {

    @Override
    public void draw() {
        Feature feature = getFeature();
        if (menuItem(feature.getName(), feature.isEnabled())) {
            feature.toggleEnabled();
        }
    }

}
