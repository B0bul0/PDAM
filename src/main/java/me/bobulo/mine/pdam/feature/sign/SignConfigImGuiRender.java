package me.bobulo.mine.pdam.feature.sign;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;

import static imgui.ImGui.checkbox;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class SignConfigImGuiRender extends AbstractFeatureBehavior implements FeatureConfigImGuiRender {

    @Override
    public void draw() {
        if (checkbox("Override Sign Gui", SignEditorFeature.OVERRIDE_SIGN_GUI.get())) {
            SignEditorFeature.OVERRIDE_SIGN_GUI.set(!SignEditorFeature.OVERRIDE_SIGN_GUI.get());
        }

        tooltip("Override the default sign editing GUI with a custom one.");

        if (checkbox("Enable Sign Viewer", SignEditorFeature.VIEW_GUI.get())) {
            SignEditorFeature.VIEW_GUI.set(!SignEditorFeature.VIEW_GUI.get());
        }

        tooltip("Enable viewing of raw sign text when right-clicking with shift.");
    }

}
