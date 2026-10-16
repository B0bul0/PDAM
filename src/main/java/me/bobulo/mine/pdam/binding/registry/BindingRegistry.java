package me.bobulo.mine.pdam.binding.registry;

import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.binding.context.BindingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Registers bindings immediately and owns their disposal. Access from the owning lifecycle thread.
 */
public final class BindingRegistry {

    private final BindingContext context;
    private final List<Binding<?>> bindings = new ArrayList<>();

    /**
     * Creates a registry, for example {@code new BindingRegistry(new BindingContext(scope))}.
     */
    public BindingRegistry(@NotNull BindingContext context) {
        this.context = context;
    }

    /**
     * Initializes and records a binding, for example {@code registry.registerBinding(binding)}.
     */
    public void registerBinding(@NotNull Binding<?> binding) {
        if (bindings.contains(binding)) {
            throw new IllegalArgumentException("Binding already registered: " + binding);
        }

        binding.init(context);
        bindings.add(binding);
    }

    /**
     * Registers bindings in order, for example {@code registry.registerBindings(first, second)}.
     */
    public void registerBindings(@NotNull Binding<?>... bindings) {
        for (Binding<?> binding : bindings) {
            registerBinding(binding);
        }
    }

    /**
     * Registers a collection in order, for example {@code registry.registerBindings(bindings)}.
     */
    public void registerBindings(@NotNull Collection<? extends Binding<?>> bindings) {
        for (Binding<?> binding : bindings) {
            registerBinding(binding);
        }
    }

    /**
     * Removes the first exact class match, for example {@code registry.unregisterBinding(type)}.
     */
    public void unregisterBinding(@NotNull Class<? extends Binding<?>> bindingClass) {
        for (Binding<?> binding : bindings) {
            if (binding.getClass().equals(bindingClass)) {
                unregisterBinding(binding);
                return;
            }
        }

        throw new IllegalArgumentException("Binding not registered: " + bindingClass.getName());
    }

    /**
     * Removes and closes a binding, for example {@code registry.unregisterBinding(binding)}.
     */
    public void unregisterBinding(@NotNull Binding<?> binding) {
        if (!bindings.remove(binding)) {
            throw new IllegalArgumentException("Binding not registered: " + binding);
        }

        binding.close();
    }

    /**
     * Removes bindings in order, for example {@code registry.unregisterBindings(first, second)}.
     */
    public void unregisterBindings(@NotNull Binding<?>... bindings) {
        for (Binding<?> binding : bindings) {
            unregisterBinding(binding);
        }
    }

    /**
     * Removes a collection in order, for example {@code registry.unregisterBindings(bindings)}.
     */
    public void unregisterBindings(@NotNull Collection<? extends Binding<?>> bindings) {
        for (Binding<?> binding : bindings) {
            unregisterBinding(binding);
        }
    }

    /**
     * Closes all registered bindings, for example during {@code scope.cleanup()}.
     */
    public void unregisterAll() {
        for (Binding<?> binding : bindings) {
            binding.close();
        }
        bindings.clear();
    }

    /**
     * Returns the live list for inspection, for example {@code registry.getBindings().size()}.
     * Do not modify it directly: registration methods own initialization and disposal.
     */
    public @NotNull List<Binding<?>> getBindings() {
        return bindings;
    }
}
