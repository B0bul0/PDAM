package me.bobulo.mine.pdam.feature.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Releases feature state before its load scope is cleaned up.
 */
public final class FeatureUninitializeContext extends FeatureLifecycleContext {

    /**
     * Creates the phase context, e.g. {@code new FeatureUninitializeContext(scope)}.
     */
    public FeatureUninitializeContext(@NotNull Scope scope) {
        super(scope);
    }
}
