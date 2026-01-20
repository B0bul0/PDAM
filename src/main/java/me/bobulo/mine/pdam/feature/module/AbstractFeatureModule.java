package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.feature.Feature;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of a FeatureModule.
 */
public abstract class AbstractFeatureModule implements FeatureModule {

    private Feature feature;
    private boolean enabled = false;

    private final List<FeatureModule> childModules = new ArrayList<>(6);

    public Feature getFeature() {
        return feature;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void enable(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == null || this.feature == feature,
          "FeatureModule is initialized with a different Feature");

        if (this.feature == null) {
            this.feature = feature;
            this.onInitialize();
        }

        if (!enabled) {
            this.onEnable();
            enabled = true;
        }
    }

    @Override
    public final void disable(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature");

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

    // Child Modules Management

    public void addChildModule(@NotNull FeatureModule module) {
        Validate.notNull(module, "FeatureModule cannot be null");
        Validate.isTrue(!childModules.contains(module), "FeatureModule is already a child");
        Validate.isTrue(module != this, "FeatureModule cannot be a child of itself");
        Validate.isTrue(feature != null, "Parent Feature is not initialized");

        childModules.add(module);
        feature.addModule(module);
    }

    public void removeChildModule(@NotNull FeatureModule module) {
        Validate.notNull(module, "FeatureModule cannot be null");
        Validate.isTrue(feature != null, "Parent Feature is not initialized");

        childModules.remove(module);
        feature.removeModule(module);
    }

}
