package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.feature.Feature;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation of a FeatureModule.
 */
public abstract class AbstractFeatureModule implements FeatureModule {

    private Feature feature; // Initialized in onAttach
    private boolean enabled = false;

    public Feature getFeature() {
        return feature;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void onAttach(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == null || this.feature == feature,
          "FeatureModule is initialized with a different Feature");

        if (this.feature == null) {
            this.feature = feature;
            this.onInitialize();
        }
    }

    @Override
    public void onDetach(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature");
        Validate.isTrue(!this.enabled,
          "FeatureModule must be disabled before detaching from Feature");

        this.feature = null;
    }

    @Override
    public final void enable(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature != null,
          "FeatureModule not initialized with a Feature");
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature");

        if (!enabled) {
            this.onEnable();
            enabled = true;
        }
    }

    @Override
    public final void disable(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature != null,
          "FeatureModule not initialized with a Feature");
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature: " +
            this.feature.getId() + " != " + feature.getId());

        if (enabled) {
            this.onDisable();
            enabled = false;
        }
    }

    /**
     * Called when the FeatureModule is first initialized.
     */
    protected void onInitialize() {
        // Optional
    }

    /**
     * Called when the FeatureModule is enabled.
     */
    protected abstract void onEnable();

    /**
     * Called when the FeatureModule is disabled.
     */
    protected abstract void onDisable();

}
