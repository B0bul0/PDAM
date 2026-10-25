package me.bobulo.mine.pdam.feature.signfinder;

import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.FeatureToolbarMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

/**
 * Groups the sign finder window and its world overlay.
 */
public final class SignFinderFeature extends AbstractFeature {

    public static final String FEATURE_ID = "sign_finder";

    private ImGuiListenerBinding[] windows;
    private ForgeListenerBinding[] listeners;

    public SignFinderFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        windows = ImGuiListenerBinding.of(
          new SignFinderWindow()
        );
        listeners = ForgeListenerBinding.of(
          new SignESP()
        );
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        context.getScope().registerBehavior(new FeatureToolbarMenuImGuiRender());
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(windows);
        context.getScope().registerBindings(listeners);
    }
}
