package me.bobulo.mine.pdam.feature.behavior.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Enables a behavior using the current feature activation scope, or the directly activated owner.
 */
public final class FeatureBehaviorEnableContext extends FeatureBehaviorContext {

    /**
     * Creates an activation context, e.g. {@code new FeatureBehaviorEnableContext(enableScope)}.
     */
    public FeatureBehaviorEnableContext(@NotNull Scope scope) {
        super(scope);
    }
}
