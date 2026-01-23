package me.bobulo.mine.pdam.feature.context;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.FeatureImpl;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for defining a Feature's context.
 */
public abstract class FeatureContext {

    @NotNull
    private final Feature feature;

    protected FeatureContext(@NotNull String featureId) {
        this.feature = new FeatureImpl(featureId);
    }

    @NotNull
    public String getFeatureId() {
        return feature.getId();
    }

    @NotNull
    public Feature getFeature() {
        return feature;
    }

    /* Module Management */

    protected void addModules(@NotNull FeatureModule... modules) {
        for (FeatureModule module : modules) {
            feature.addModule(module);
        }
    }

    protected void addModule(@NotNull FeatureModule module) {
        feature.addModule(module);
    }

    /* Configuration */

    protected ConfigProperty<Boolean> createEnabledConfig(boolean defaultValue) {
        return ConfigProperty.of(
          feature.getId() + ".enabled",
          defaultValue
        ).onChange(enabled -> {
            if (enabled) {
                getFeature().enable();
            } else {
                getFeature().disable();
            }
        });
    }

    protected <V> ConfigValue<V> createConfigValue(String key, V defaultValue) {
        return ConfigProperty.of(
          feature.getId() + "." + key,
          defaultValue
        );
    }

}
