package me.bobulo.mine.pdam.feature.sign;

import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;

import static imgui.ImGui.checkbox;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class SignConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    @Override
    public void draw() {
        if (checkbox("Override Sign Gui", SignEditor.OVERRIDE_SIGN_GUI.get())) {
            SignEditor.OVERRIDE_SIGN_GUI.set(!SignEditor.OVERRIDE_SIGN_GUI.get());
        }

        tooltip("Override the default sign editing GUI with a custom one.");

        if (checkbox("Enable Sign Viewer", SignEditor.VIEW_GUI.get())) {
            SignEditor.VIEW_GUI.set(!SignEditor.VIEW_GUI.get());
        }

        tooltip("Enable viewing of raw sign text when right-clicking with shift.");
    }

}
