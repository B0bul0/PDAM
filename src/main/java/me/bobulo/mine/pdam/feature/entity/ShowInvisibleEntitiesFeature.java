package me.bobulo.mine.pdam.feature.entity;

import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import org.jetbrains.annotations.NotNull;

public final class ShowInvisibleEntitiesFeature extends AbstractFeature {

    public static final String FEATURE_ID = "show_invisible_entities";

    // Feature to show invisible entities
    public static boolean showInvisibleEntities = true;

    public ShowInvisibleEntitiesFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
    }

    @Override
    protected void onDisable(@NotNull FeatureDisableContext context) {
        showInvisibleEntities = false;
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        showInvisibleEntities = true;
    }
}
