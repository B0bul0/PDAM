package me.bobulo.mine.devmod.feature.component;

import me.bobulo.mine.devmod.feature.Feature;
import org.apache.commons.lang3.Validate;

public abstract class AbstractFeatureComponent implements FeatureComponent {

    private Feature feature;
    private boolean enabled = false;

    @Override
    public final void init(Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.isTrue(this.feature == null, "FeatureComponent is already initialized");

        this.feature = feature;
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

    public abstract void onEnable();

    public abstract void onDisable();

}
