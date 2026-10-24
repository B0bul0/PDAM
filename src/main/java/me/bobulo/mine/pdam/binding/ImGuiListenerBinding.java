package me.bobulo.mine.pdam.binding;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.binding.context.BindingContext;
import me.bobulo.mine.pdam.imgui.ImGuiRenderer;

import java.util.Objects;

/**
 * Owns the registration of a single ImGui listener object.
 */
public final class ImGuiListenerBinding implements Binding<Object> {
    private final Object listener;
    private final ImGuiRenderer renderer;
    private boolean registered;

    /**
     * Binds one object, e.g. new ImGuiListenerBinding(listener).
     */
    public ImGuiListenerBinding(Object listener) {
        this(listener, PDAM.getImGuiRenderer());
    }

    /**
     * Binds one object to an explicit registration service.
     */
    public ImGuiListenerBinding(Object listener, ImGuiRenderer renderer) {
        this.listener = Objects.requireNonNull(listener, "Listener");
        this.renderer = Objects.requireNonNull(renderer, "Registration service");
    }

    /**
     * Creates one binding per object, e.g. scope.registerBindings(ImGuiListenerBinding.of(first, second)).
     */
    public static ImGuiListenerBinding[] of(Object... listeners) {
        ImGuiListenerBinding[] bindings = new ImGuiListenerBinding[listeners.length];
        for (int i = 0; i < listeners.length; i++)
            bindings[i] = new ImGuiListenerBinding(listeners[i]);
        return bindings;
    }

    @Override
    public void init(BindingContext context) {
        Objects.requireNonNull(context, "Binding context");
        if (registered) {
            throw new IllegalStateException("Listener already registered: " + listener);
        }
        renderer.registerWindow(listener);
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
        renderer.unregisterWindow(listener);
    }
}
