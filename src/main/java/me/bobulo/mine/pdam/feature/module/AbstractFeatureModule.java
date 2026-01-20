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

    @Override
    public final void init(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == null, "FeatureModule is already initialized");

        this.feature = feature;
        onInit();
    }

    public Feature getFeature() {
        return feature;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void enable() {
        if (!enabled) {
            this.onEnable();
            enabled = true;
        }
    }

    @Override
    public final void disable() {
        if (enabled) {
            this.onDisable();
            enabled = false;
        }
    }


    /**
     * Called during initialization of the FeatureModule.
     */
    protected void onInit() {
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
        Validate.isTrue(feature instanceof ModularFeature, "Parent Feature is not ModularFeature");
        ModularFeature modularFeature = (ModularFeature) feature;

        childModules.add(module);
        modularFeature.addModule(module);
    }

    public void removeChildModule(@NotNull FeatureModule module) {
        Validate.notNull(module, "FeatureModule cannot be null");
        Validate.isTrue(feature instanceof ModularFeature, "Parent Feature is not ModularFeature");
        ModularFeature modularFeature = (ModularFeature) feature;

        childModules.remove(module);
        modularFeature.removeModule(module);
    }

}
