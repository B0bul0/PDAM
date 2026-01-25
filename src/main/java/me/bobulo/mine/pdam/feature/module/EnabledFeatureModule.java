package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.config.ConfigProperty;

/**
 * A feature module that manages the enabled/disabled state of a feature through a configuration property.
 */
public final class EnabledFeatureModule extends AbstractFeatureModule {

    private final boolean defaultEnabled;
    private ConfigProperty<Boolean> enabledConfig;

    public EnabledFeatureModule(boolean defaultEnabled) {
        this.defaultEnabled = defaultEnabled;
    }

    public ConfigProperty<Boolean> getEnabledConfig() {
        return enabledConfig;
    }

    @Override
    protected void onInitialize() { // on attach
        this.enabledConfig = ConfigProperty.of(getFeature().getId() + ".enabled", defaultEnabled)
          .onChange(enabled -> {
                if (enabled) {
                    getFeature().enable();
                } else {
                    getFeature().disable();
                }
          });

        if (enabledConfig.get()) {
            getFeature().enable();
        }
    }

    @Override
    protected void onEnable() {
        if (enabledConfig != null && !enabledConfig.get()) {
            enabledConfig.set(true);
        }
    }

    @Override
    protected void onDisable() {
        if (enabledConfig != null && enabledConfig.get()) {
            enabledConfig.set(false);
        }
    }
}
