package me.bobulo.mine.pdam.feature.context;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Gives lifecycle callbacks access to their feature and phase scope on the game thread.
 */
public abstract class FeatureLifecycleContext {
    private final Scope scope;

    protected FeatureLifecycleContext(@NotNull Scope scope) {
        this.scope = Objects.requireNonNull(scope, "Scope");
        scope.requireFeature();
    }

    /**
     * Returns the owning feature, e.g. {@code context.getFeature().getId()}.
     */
    public final @NotNull Feature getFeature() {
        return scope.requireFeature();
    }

    /**
     * Returns this phase's scope, e.g. {@code context.getScope().registerBinding(binding)}.
     * Resources remain owned by the scope and follow its existing cleanup lifecycle.
     */
    public final @NotNull Scope getScope() {
        return scope;
    }
}
