package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorAttachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDetachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a behavior that can be attached to a {@link Feature}.
 */
public interface FeatureBehavior {

    /**
     * Called when the behavior is attached to a feature.
     * The context exposes the scope that owns this registration.
     *
     * @param context The owning feature and scope for attachment resources.
     */
    default void onAttach(@NotNull FeatureBehaviorAttachContext context) {
    }

    /**
     * Called when the behavior is detached from a feature.
     * This runs after disable and before the owning scope is cleaned up.
     *
     * @param context The owning feature and scope for attachment resources.
     */
    default void onDetach(@NotNull FeatureBehaviorDetachContext context) {
    }

    /**
     * Activates this behavior, for example when its feature is enabled.
     * Register activation resources through {@code context.getScope()}.
     */
    default void enable(@NotNull FeatureBehaviorEnableContext context) {
    }

    /**
     * Deactivates this behavior before activation resources are released.
     * The activation scope remains available throughout this callback.
     */
    default void disable(@NotNull FeatureBehaviorDisableContext context) {
    }
}
