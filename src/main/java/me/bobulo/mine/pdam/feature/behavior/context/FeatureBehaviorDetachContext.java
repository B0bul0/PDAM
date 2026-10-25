package me.bobulo.mine.pdam.feature.behavior.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Detaches a behavior before its owning scope releases the registered resources.
 */
public final class FeatureBehaviorDetachContext extends FeatureBehaviorContext {

    /**
     * Creates a detachment context, e.g. {@code new FeatureBehaviorDetachContext(scope)}.
     */
    public FeatureBehaviorDetachContext(@NotNull Scope scope) {
        super(scope);
    }
}
