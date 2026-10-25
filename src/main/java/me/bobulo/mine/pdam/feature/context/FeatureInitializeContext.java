package me.bobulo.mine.pdam.feature.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Initializes persistent resources in the load scope.
 */
public final class FeatureInitializeContext extends FeatureLifecycleContext {

    /**
     * Creates the phase context, e.g. {@code new FeatureInitializeContext(scope)}.
     */
    public FeatureInitializeContext(@NotNull Scope scope) {
        super(scope);
    }
}
