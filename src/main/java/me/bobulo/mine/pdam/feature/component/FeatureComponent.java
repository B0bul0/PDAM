package me.bobulo.mine.pdam.feature.component;

import me.bobulo.mine.pdam.config.PropertyDeclarer;
import me.bobulo.mine.pdam.feature.Feature;

/**
 * Represents a modular component of a {@link Feature}.
 * <p>
 * A feature can be composed of multiple components, each handling a specific piece of logic,
 * such as event listening. Components share the lifecycle of their parent feature.
 */
public interface FeatureComponent extends PropertyDeclarer {

    /**
     * Initializes the component with its parent feature.
     * This method is called by the feature that owns this component and should only be called once.
     *
     * @param feature The parent feature that this component belongs to.
     */
    void init(Feature feature);

    /**
     * Checks if the component is currently enabled.
     *
     * @return {@code true} if the component is enabled, {@code false} otherwise.
     */
    boolean isEnabled();

    /**
     * Enables the component, activating its functionality.
     * This is typically called when the parent feature is enabled.
     */
    void enable();

    /**
     * Disables the component, deactivating its functionality.
     * This is typically called when the parent feature is disabled.
     */
    void disable();

}