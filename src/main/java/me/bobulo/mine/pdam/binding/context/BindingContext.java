package me.bobulo.mine.pdam.binding.context;

import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Context supplied to a binding without requiring a feature or configuration.
 */
public final class BindingContext {

    private final Scope scope;

    /**
     * Creates the context used by a scope, for example {@code new BindingContext(scope)}.
     */
    public BindingContext(@NotNull Scope scope) {
        this.scope = scope;
    }

    /**
     * Returns the owning scope, for example to inspect {@code context.getScope().getBindings()}.
     */
    public @NotNull Scope getScope() {
        return scope;
    }
}
