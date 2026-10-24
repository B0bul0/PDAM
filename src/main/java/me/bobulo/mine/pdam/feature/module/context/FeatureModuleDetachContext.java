package me.bobulo.mine.pdam.feature.module.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Detaches a module before its owning scope releases the registered resources.
 */
public final class FeatureModuleDetachContext extends FeatureModuleContext {

    /**
     * Creates a detachment context, e.g. {@code new FeatureModuleDetachContext(scope)}.
     */
    public FeatureModuleDetachContext(@NotNull Scope scope) {
        super(scope);
    }
}
