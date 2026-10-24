package me.bobulo.mine.pdam.feature.module.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Disables a module while its feature activation scope, or directly activated owner, is available.
 */
public final class FeatureModuleDisableContext extends FeatureModuleContext {

    /**
     * Creates a deactivation context, e.g. {@code new FeatureModuleDisableContext(enableScope)}.
     */
    public FeatureModuleDisableContext(@NotNull Scope scope) {
        super(scope);
    }
}
