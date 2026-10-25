package me.bobulo.mine.pdam.feature.player;

import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.FeatureToolbarMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

/**
 * Exposes flight controls through a persistent toolbar window.
 */
public final class FlyBoosterFeature extends AbstractFeature {

    public static final String FEATURE_ID = "fly_booster";

    private ImGuiListenerBinding[] windows;

    public FlyBoosterFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        windows = ImGuiListenerBinding.of(new FlyBoosterWindow());
        context.getScope().registerBehavior(new EnabledFeatureBehavior(false));
        context.getScope().registerBehavior(new FeatureToolbarMenuImGuiRender());
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(windows);
    }
}
