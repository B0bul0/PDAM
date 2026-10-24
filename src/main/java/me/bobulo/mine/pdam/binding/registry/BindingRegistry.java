package me.bobulo.mine.pdam.binding.registry;

import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.binding.context.BindingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        this.context = Objects.requireNonNull(context, "Binding context");
    }

    /**
     * Initializes and records a binding, for example {@code registry.registerBinding(binding)}.
     */
    public void registerBinding(@NotNull Binding<?> binding) {
        Objects.requireNonNull(binding, "Binding");
        if (indexOf(binding) >= 0) {
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
        int index = indexOf(binding);
        if (index < 0) {
            throw new IllegalArgumentException("Binding not registered: " + binding);
        }

        bindings.remove(index);
        binding.close();
    }

    private int indexOf(Binding<?> binding) {
        for (int i = 0; i < bindings.size(); i++) {
            if (bindings.get(i) == binding) {
                return i;
            }
        }
        return -1;
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
        RuntimeException failure = null;
        while (!bindings.isEmpty()) {
            Binding<?> binding = bindings.remove(0);
            try {
                binding.close();
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else if (failure != exception) {
                    failure.addSuppressed(exception);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }

    /**
     * Returns the first assignable bound value, e.g. registry.get(Listener.class), or null.
     */
    public <T> T get(@NotNull Class<T> type) {
        Objects.requireNonNull(type, "Bound value type");
        for (Binding<?> binding : bindings) {
            Object value = binding.get();
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        }
        return null;
    }

    /**
     * Returns all assignable bound values in registration order, e.g. registry.gets(Listener.class).
     */
    public <T> List<T> gets(@NotNull Class<T> type) {
        Objects.requireNonNull(type, "Bound value type");
        List<T> values = new ArrayList<>();
        for (Binding<?> binding : bindings) {
            Object value = binding.get();
            if (type.isInstance(value)) {
                values.add(type.cast(value));
            }
        }
        return values;
    }

    /**
     * Returns the live list for inspection, for example {@code registry.getBindings().size()}.
     * Do not modify it directly: registration methods own initialization and disposal.
     */
    public @NotNull List<Binding<?>> getBindings() {
        return bindings;
    }
}
