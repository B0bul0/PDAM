package me.bobulo.mine.pdam.feature.skin;

import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import org.jetbrains.annotations.NotNull;

/**
 * Groups skin extraction from players, placed heads and held skulls.
 */
public final class SkinExtractionFeature extends AbstractFeature {

    public static final String FEATURE_ID = "skin_extraction";

    private ForgeListenerBinding[] listeners;

    public SkinExtractionFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        listeners = ForgeListenerBinding.of(
          new PlayerSkinExtractionListener(),
          new HeadWorldSkinExtractionListener(),
          new HotBarSkinExtractionListener()
        );
        context.getScope().registerBehavior(new EnabledFeatureBehavior(false));
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(listeners);
    }
}
