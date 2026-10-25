package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.feature.module.context.FeatureModuleAttachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import org.jetbrains.annotations.NotNull;

/**
 * A unique feature component, indexed by its exact concrete type in a scope.
 * This contract is independent of FeatureBehavior; listener registration belongs to bindings.
 */
public interface FeatureModule {

    /**
     * Attaches this unique component, e.g. scope.registerModule(module).
     * The context exposes the scope that owns this registration.
     */
    default void onAttach(@NotNull FeatureModuleAttachContext context) {
    }

    /**
     * Releases this component before its owning scope is cleaned up.
     */
    default void onDetach(@NotNull FeatureModuleDetachContext context) {
    }

    /**
     * Enables the module, activating its functionality.
     * This is typically called when the parent feature is enabled.
     * Register activation resources through {@code context.getScope()}.
     */
    void enable(@NotNull FeatureModuleEnableContext context);

    /**
     * Disables the module, deactivating its functionality.
     * This is typically called when the parent feature is disabled.
     * The activation scope remains available until all disable callbacks finish.
     */
    void disable(@NotNull FeatureModuleDisableContext context);

}
