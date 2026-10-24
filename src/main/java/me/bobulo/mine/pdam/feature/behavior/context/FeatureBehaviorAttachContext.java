package me.bobulo.mine.pdam.feature.behavior.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Attaches a behavior using the scope that owns its registration.
 */
public final class FeatureBehaviorAttachContext extends FeatureBehaviorContext {

    /**
     * Creates an attachment context, e.g. {@code new FeatureBehaviorAttachContext(scope)}.
     */
    public FeatureBehaviorAttachContext(@NotNull Scope scope) {
        super(scope);
    }
}
