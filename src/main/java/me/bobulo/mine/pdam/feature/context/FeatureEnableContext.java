package me.bobulo.mine.pdam.feature.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Activates the feature using a fresh scope for this activation.
 */
public final class FeatureEnableContext extends FeatureLifecycleContext {

    /**
     * Creates the phase context, e.g. {@code new FeatureEnableContext(scope)}.
     */
    public FeatureEnableContext(@NotNull Scope scope) {
        super(scope);
    }
}
