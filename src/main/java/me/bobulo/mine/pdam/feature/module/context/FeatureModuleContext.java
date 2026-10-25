package me.bobulo.mine.pdam.feature.module.context;

import me.bobulo.mine.pdam.feature.context.FeatureLifecycleContext;
import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Gives module callbacks access to their feature and phase scope on the game thread.
 */
public abstract class FeatureModuleContext extends FeatureLifecycleContext {

    protected FeatureModuleContext(@NotNull Scope scope) {
        super(scope);
    }
}
