package me.bobulo.mine.pdam.feature.world;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.ConfigMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

public final class WorldTimeFeature extends AbstractFeature {

    public static final String FEATURE_ID = "world_time";

    public static final ConfigValue<Integer> WORLD_TIME = ConfigProperty.of(FEATURE_ID + ".world_time", 0);

    public static boolean isFeatureEnabled() {
        return PDAM.getFeatureService().isFeatureEnabled(FEATURE_ID);
    }

    public WorldTimeFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(false));
        context.getScope().registerBehavior(new WorldTimeConfigImGuiRender());
        context.getScope().registerBehavior(new ConfigMenuImGuiRender(true));
    }
}
