package me.bobulo.mine.pdam.binding;

import me.bobulo.mine.pdam.binding.context.BindingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Owns the registration and disposal of a value, independently of its consumer.
 * A scope initializes registered bindings and closes them during cleanup.
 *
 * @param <T> the type of value supplied by the binding
 */
public interface Binding<T> extends AutoCloseable {

    /**
     * Initializes this binding and its resources in the supplied context.
     * Called by the registry, for example through {@code scope.registerBinding(binding)}.
     */
    void init(@NotNull BindingContext context);

    /**
     * Supplies the bound value; caching is determined by the implementation.
     * For example, {@code binding.get()} retrieves the registered resource.
     */
    T get();

    /**
     * Unregisters and releases the resources owned by this binding.
     * Called on removal or cleanup, for example through {@code scope.cleanup()}.
     */
    @Override
    void close();
}
