package me.bobulo.mine.pdam.binding;

import me.bobulo.mine.pdam.binding.context.BindingContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Owns the registration of a single Forge event listener object.
 */
public final class ForgeListenerBinding implements Binding<Object> {
    private final Object listener;
    private final EventBus eventBus;
    private boolean registered;

    /**
     * Binds one object, e.g. new ForgeListenerBinding(listener).
     */
    public ForgeListenerBinding(Object listener) {
        this(listener, MinecraftForge.EVENT_BUS);
    }

    /**
     * Binds one object to an explicit registration service.
     */
    public ForgeListenerBinding(Object listener, EventBus eventBus) {
        this.listener = Objects.requireNonNull(listener, "Listener");
        this.eventBus = Objects.requireNonNull(eventBus, "Registration service");
    }

    /**
     * Creates one binding per object, e.g. scope.registerBindings(ForgeListenerBinding.of(first, second)).
     */
    public static ForgeListenerBinding[] of(Object... listeners) {
        ForgeListenerBinding[] bindings = new ForgeListenerBinding[listeners.length];
        for (int i = 0; i < listeners.length; i++)
            bindings[i] = new ForgeListenerBinding(listeners[i]);
        return bindings;
    }

    @Override
    public void init(@NotNull BindingContext context) {
        Objects.requireNonNull(context, "Binding context");
        if (registered) {
            throw new IllegalStateException("Listener already registered: " + listener);
        }
        eventBus.register(listener);
        registered = true;
    }

    @Override
    public Object get() {
        return listener;
    }

    @Override
    public void close() {
        if (!registered) {
            return;
        }
        registered = false;
        eventBus.unregister(listener);
    }
}
