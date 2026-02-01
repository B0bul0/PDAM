package me.bobulo.mine.pdam.feature.world;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;

public final class WorldTime {

    public static final String FEATURE_ID = "world_time";

    public static final ConfigValue<Integer> WORLD_TIME = ConfigProperty.of(FEATURE_ID + ".world_time", 0);

    public static boolean isFeatureEnabled() {
        return PDAM.getFeatureService().isFeatureEnabled(FEATURE_ID);
    }

}
