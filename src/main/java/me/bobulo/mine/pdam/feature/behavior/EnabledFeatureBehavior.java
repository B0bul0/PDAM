package me.bobulo.mine.pdam.feature.behavior;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorAttachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import org.jetbrains.annotations.NotNull;

/**
 * A feature behavior that manages the enabled/disabled state of a feature through a configuration property.
 */
public final class EnabledFeatureBehavior extends AbstractFeatureBehavior {

    private final boolean defaultEnabled;
    private ConfigProperty<Boolean> enabledConfig;

    public EnabledFeatureBehavior(boolean defaultEnabled) {
        this.defaultEnabled = defaultEnabled;
    }

    public ConfigProperty<Boolean> getEnabledConfig() {
        return enabledConfig;
    }

    @Override
    protected void onInitialize(@NotNull FeatureBehaviorAttachContext context) { // on attach
        this.enabledConfig = ConfigProperty.of(context.getFeature().getId() + ".enabled", defaultEnabled)
          .onChange(enabled -> {
              if (enabled) {
                  getFeature().enable();
              } else {
                  getFeature().disable();
              }
          });

        if (enabledConfig.get()) {
            context.getFeature().enable();
        }
    }

    @Override
    protected void onEnable(@NotNull FeatureBehaviorEnableContext context) {
        if (enabledConfig != null && !enabledConfig.get()) {
            enabledConfig.set(true);
        }
    }

    @Override
    protected void onDisable(@NotNull FeatureBehaviorDisableContext context) {
        if (enabledConfig != null && enabledConfig.get()) {
            enabledConfig.set(false);
        }
    }
}
