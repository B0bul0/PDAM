package me.bobulo.mine.pdam.feature.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Deactivates the feature before its activation scope is cleaned up.
 */
public final class FeatureDisableContext extends FeatureLifecycleContext {

    /**
     * Creates the phase context, e.g. {@code new FeatureDisableContext(scope)}.
     */
    public FeatureDisableContext(@NotNull Scope scope) {
        super(scope);
    }
}
