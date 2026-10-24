package me.bobulo.mine.pdam.binding;

import me.bobulo.mine.pdam.binding.context.BindingContext;
import me.bobulo.mine.pdam.imgui.ImGuiRenderer;
import me.bobulo.mine.pdam.scope.Scope;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ListenerBindingsTest {
    @Test
    void ca12ForgeBindingRegistersAndUnregistersOneArbitraryObjectPerLifecycle() {
        EventBus bus = mock(EventBus.class);
        Object listener = new Object();
        ForgeListenerBinding binding = new ForgeListenerBinding(listener, bus);
        Scope scope = new Scope();
        scope.registerBinding(binding);
        assertSame(listener, binding.get());
        assertSame(listener, scope.get(Object.class));
        verify(bus).register(listener);
        assertThrows(IllegalStateException.class, () -> binding.init(new BindingContext(scope)));
        scope.cleanup();
        binding.close();
        verify(bus).unregister(listener);
        scope.registerBinding(binding);
        scope.cleanup();
        verify(bus, times(2)).register(listener);
        verify(bus, times(2)).unregister(listener);
    }

    @Test
    void ca12ImGuiBindingRegistersAndUnregistersOneArbitraryObject() {
        ImGuiRenderer renderer = mock(ImGuiRenderer.class);
        Object listener = new Object();
        ImGuiListenerBinding binding = new ImGuiListenerBinding(listener, renderer);
        Scope scope = new Scope();
        scope.registerBinding(binding);
        assertSame(listener, binding.get());
        verify(renderer).registerWindow(listener);
        assertThrows(IllegalStateException.class, () -> binding.init(new BindingContext(scope)));
        scope.cleanup();
        binding.close();
        verify(renderer).unregisterWindow(listener);
    }

    @Test
    void ca12BindingRejectsNullListenersAndRegistrationServices() {
        EventBus bus = mock(EventBus.class);
        ImGuiRenderer renderer = mock(ImGuiRenderer.class);
        assertThrows(NullPointerException.class, () -> new ForgeListenerBinding(null, bus));
        assertThrows(NullPointerException.class, () -> new ForgeListenerBinding(new Object(), null));
        assertThrows(NullPointerException.class, () -> new ImGuiListenerBinding(null, renderer));
        assertThrows(NullPointerException.class, () -> new ImGuiListenerBinding(new Object(), null));
    }
}
