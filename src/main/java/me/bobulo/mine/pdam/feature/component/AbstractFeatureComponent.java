package me.bobulo.mine.pdam.feature.component;

import me.bobulo.mine.pdam.feature.Feature;
import org.apache.commons.lang3.Validate;

/**
 * Base implementation of a FeatureComponent.
 */
public abstract class AbstractFeatureComponent implements FeatureComponent {

    private Feature feature;
    private boolean enabled = false;

    @Override
    public final void init(Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == null, "FeatureComponent is already initialized");

        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
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

    protected abstract void onEnable();

    protected abstract void onDisable();

}
