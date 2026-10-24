package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.FeatureBehavior;

/**
 * Represents a module responsible for rendering ImGui items in the menu.
 */
public interface MenuImGuiRender extends FeatureBehavior {

    /**
     * Draws the ImGui menu items.
     */
    void draw();

}
