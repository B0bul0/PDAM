package me.bobulo.mine.pdam.scope;

import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.binding.context.BindingContext;
import me.bobulo.mine.pdam.binding.registry.BindingRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Owns a group of bindings for a lifecycle, with no dependency on features.
 */
public final class Scope {

    private final BindingRegistry bindingRegistry = new BindingRegistry(new BindingContext(this));

    /**
     * Registers a binding immediately, for example {@code scope.registerBinding(binding)}.
     */
    public @NotNull Scope registerBinding(@NotNull Binding<?> binding) {
        bindingRegistry.registerBinding(binding);
        return this;
    }

    /**
     * Removes and closes a binding, for example {@code scope.unregisterBinding(binding)}.
     */
    public @NotNull Scope unregisterBinding(@NotNull Binding<?> binding) {
        bindingRegistry.unregisterBinding(binding);
        return this;
    }

    /**
     * Returns the live binding list for inspection, for example {@code scope.getBindings().size()}.
     * Use the registration methods to modify its contents.
     */
    public @NotNull List<Binding<?>> getBindings() {
        return bindingRegistry.getBindings();
    }

    /**
     * Releases registered resources, for example {@code scope.cleanup()} at the end of a lifecycle.
     */
    public void cleanup() {
        bindingRegistry.unregisterAll();
    }
}
