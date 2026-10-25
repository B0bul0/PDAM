package me.bobulo.mine.pdam.feature.player;

import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.FeatureToolbarMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

/**
 * Keeps the coordinate window available through the toolbar.
 */
public final class PlayerUtilitiesFeature extends AbstractFeature {

    public static final String FEATURE_ID = "player_utilities";

    private ImGuiListenerBinding[] windows;

    public PlayerUtilitiesFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        windows = ImGuiListenerBinding.of(
          new CoordinatesWindow()
        );
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        context.getScope().registerBehavior(new FeatureToolbarMenuImGuiRender());
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(windows);
    }
}
