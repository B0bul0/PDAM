package me.bobulo.mine.pdam.feature.entity;

import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import org.jetbrains.annotations.NotNull;

/**
 * Displays inspected entity information through the overlay listener.
 */
public final class EntityInfoOverlayFeature extends AbstractFeature {

    public static final String FEATURE_ID = "entity_info_overlay";

    private ForgeListenerBinding[] listeners;

    public EntityInfoOverlayFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        listeners = ForgeListenerBinding.of(new EntityOverlayInfoListener());
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(listeners);
    }
}
