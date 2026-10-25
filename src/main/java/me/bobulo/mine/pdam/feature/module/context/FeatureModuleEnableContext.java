package me.bobulo.mine.pdam.feature.module.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Enables a module using the current feature activation scope, or the directly activated owner.
 */
public final class FeatureModuleEnableContext extends FeatureModuleContext {

    /**
     * Creates an activation context, e.g. {@code new FeatureModuleEnableContext(enableScope)}.
     */
    public FeatureModuleEnableContext(@NotNull Scope scope) {
        super(scope);
    }
}
