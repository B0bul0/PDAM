package me.bobulo.mine.pdam.feature.context;

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
        setup();
    }

    protected abstract void setup();

    @NotNull
    public String getFeatureId() {
        return feature.getId();
    }

    @NotNull
    public Feature getFeature() {
        return feature;
    }

    protected void addModules(@NotNull FeatureModule... modules) {
        for (FeatureModule module : modules) {
            feature.addModule(module);
        }
    }

    protected void addModule(@NotNull FeatureModule module) {
        feature.addModule(module);
    }

}
