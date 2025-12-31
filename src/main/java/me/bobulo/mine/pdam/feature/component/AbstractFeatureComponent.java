package me.bobulo.mine.pdam.feature.component;

import me.bobulo.mine.pdam.feature.ComponentableFeature;
import me.bobulo.mine.pdam.feature.Feature;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of a FeatureComponent.
 */
public abstract class AbstractFeatureComponent implements FeatureComponent {

    private Feature feature;
    private boolean enabled = false;

    private final List<FeatureComponent> childComponents = new ArrayList<>(6);

    @Override
    public final void init(@NotNull Feature feature) {
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

    /**
     * Called when the FeatureComponent is enabled.
     */
    protected abstract void onEnable();

    /**
     * Called when the FeatureComponent is disabled.
     */
    protected abstract void onDisable();

    // Child Components Management

    public void addChildComponent(@NotNull FeatureComponent component) {
        Validate.notNull(component, "FeatureComponent cannot be null");
        Validate.isTrue(!childComponents.contains(component), "FeatureComponent is already a child");
        Validate.isTrue(component != this, "FeatureComponent cannot be a child of itself");
        Validate.isTrue(feature instanceof ComponentableFeature, "Parent Feature is not ComponentableFeature");
        ComponentableFeature componentableFeature = (ComponentableFeature) feature;

        childComponents.add(component);
        componentableFeature.addComponent(component);
    }

    public void removeChildComponent(@NotNull FeatureComponent component) {
        Validate.notNull(component, "FeatureComponent cannot be null");
        Validate.isTrue(feature instanceof ComponentableFeature, "Parent Feature is not ComponentableFeature");
        ComponentableFeature componentableFeature = (ComponentableFeature) feature;

        childComponents.remove(component);
        componentableFeature.removeComponent(component);
    }

}
