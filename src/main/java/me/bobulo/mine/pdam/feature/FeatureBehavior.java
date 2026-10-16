package me.bobulo.mine.pdam.feature;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a behavior that can be attached to a {@link Feature}.
 */
public interface FeatureBehavior {

    /**
     * Called when the behavior is attached to a feature.
     * The feature's load scope is already available for lifetime bindings.
     *
     * @param feature The feature to which this behavior is being attached.
     */
    default void onAttach(@NotNull Feature feature) {
    }

    /**
     * Called when the behavior is detached from a feature.
     * On feature removal, this runs after disable and before the load scope is cleaned up.
     *
     * @param feature The feature from which this behavior is being detached.
     */
    default void onDetach(@NotNull Feature feature) {
    }

}
