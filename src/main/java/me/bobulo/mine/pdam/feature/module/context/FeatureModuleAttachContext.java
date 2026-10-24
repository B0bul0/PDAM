package me.bobulo.mine.pdam.feature.module.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Attaches a module using the scope that owns its registration.
 */
public final class FeatureModuleAttachContext extends FeatureModuleContext {

    /**
     * Creates an attachment context, e.g. {@code new FeatureModuleAttachContext(scope)}.
     */
    public FeatureModuleAttachContext(@NotNull Scope scope) {
        super(scope);
    }
}
