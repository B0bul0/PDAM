package me.bobulo.mine.pdam.feature.behavior.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Disables a behavior while its feature activation scope, or directly activated owner, is available.
 */
public final class FeatureBehaviorDisableContext extends FeatureBehaviorContext {

    /**
     * Creates a deactivation context, e.g. {@code new FeatureBehaviorDisableContext(enableScope)}.
     */
    public FeatureBehaviorDisableContext(@NotNull Scope scope) {
        super(scope);
    }
}
