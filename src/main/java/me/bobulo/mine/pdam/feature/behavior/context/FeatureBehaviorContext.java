package me.bobulo.mine.pdam.feature.behavior.context;

import me.bobulo.mine.pdam.feature.context.FeatureLifecycleContext;
import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Gives behavior callbacks access to their feature and phase scope on the game thread.
 */
public abstract class FeatureBehaviorContext extends FeatureLifecycleContext {

    protected FeatureBehaviorContext(@NotNull Scope scope) {
        super(scope);
    }
}
