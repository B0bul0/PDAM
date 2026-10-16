package me.bobulo.mine.pdam.scope;

import me.bobulo.mine.pdam.support.RecordingBinding;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ScopeTest {

    @Test
    public void ca01RegistersBindingsInItsOwnContext() {
        Scope scope = new Scope();
        RecordingBinding binding = new RecordingBinding("resource", new ArrayList<>());

        assertSame(scope, scope.registerBinding(binding));
        assertSame(scope, binding.getContext().getScope());
        assertEquals(Collections.singletonList(binding), scope.getBindings());
    }

    @Test
    public void ca02UnregistersABindingAndSupportsFluentRegistration() {
        Scope scope = new Scope();
        RecordingBinding binding = new RecordingBinding("resource", new ArrayList<>());
        scope.registerBinding(binding);

        assertSame(scope, scope.unregisterBinding(binding));
        assertEquals(1, binding.getClosures());
        assertTrue(scope.getBindings().isEmpty());
    }

    @Test
    public void ca02CleanupClosesBindingsOnlyOnceAndAllowsAnIndependentScope() {
        Scope first = new Scope();
        Scope second = new Scope();
        RecordingBinding binding = new RecordingBinding("resource", new ArrayList<>());
        RecordingBinding independent = new RecordingBinding("independent", new ArrayList<>());
        first.registerBinding(binding);
        second.registerBinding(independent);

        first.cleanup();
        first.cleanup();

        assertEquals(1, binding.getClosures());
        assertTrue(first.getBindings().isEmpty());
        assertEquals(0, independent.getClosures());
        assertEquals(Collections.singletonList(independent), second.getBindings());
    }
}
