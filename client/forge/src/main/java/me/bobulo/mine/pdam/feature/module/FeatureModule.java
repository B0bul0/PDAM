package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.FeatureBehavior;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a modular module of a {@link Feature}.
 * <p>
 * A feature can be composed of multiple modules, each handling a specific piece of logic,
 * such as event listening. Modules share the lifecycle of their parent feature.
 */
public interface FeatureModule extends FeatureBehavior {

    /**
     * Enables the module, activating its functionality.
     * This is typically called when the parent feature is enabled.
     */
    void enable(@NotNull Feature feature);

    /**
     * Disables the module, deactivating its functionality.
     * This is typically called when the parent feature is disabled.
     */
    void disable(@NotNull Feature feature);

}