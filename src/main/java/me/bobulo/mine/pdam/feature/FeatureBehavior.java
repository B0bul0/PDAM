package me.bobulo.mine.pdam.feature;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a behavior that can be attached to a {@link Feature}.
 */
public interface FeatureBehavior {

    /**
     * Called when the behavior is attached to a feature.
     *
     * @param feature The feature to which this behavior is being attached.
     */
    default void onAttach(@NotNull Feature feature) {}

    /**
     * Called when the behavior is detached from a feature.
     *
     * @param feature The feature from which this behavior is being detached.
     */
    default void onDetach(@NotNull Feature feature) {}

}
