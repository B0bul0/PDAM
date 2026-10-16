package me.bobulo.mine.pdam.binding;

import me.bobulo.mine.pdam.binding.context.BindingContext;
import me.bobulo.mine.pdam.binding.registry.BindingRegistry;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.support.RecordingBinding;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BindingRegistryTest {

    private final Scope scope = new Scope();
    private final BindingRegistry registry = new BindingRegistry(new BindingContext(scope));
    private final List<String> calls = new ArrayList<>();
    private final RecordingBinding first = new RecordingBinding("first", calls);
    private final RecordingBinding second = new RecordingBinding("second", calls);

    @Test
    public void ca01InitializesAndProvidesAValueWithoutAFeature() {
        registry.registerBinding(first);

        assertEquals("first", first.get());
        assertSame(scope, first.getContext().getScope());
        assertEquals(1, first.getInitializations());
        assertEquals(Collections.singletonList(first), registry.getBindings());
    }

    @Test
    public void ca02RegistersAndUnregistersVarargs() {
        registry.registerBindings(first, second);
        registry.unregisterBindings(first, second);

        assertEquals(Arrays.asList("init:first", "init:second", "close:first", "close:second"), calls);
        assertTrue(registry.getBindings().isEmpty());
    }

    @Test
    public void ca02RegistersAndUnregistersCollections() {
        registry.registerBindings(Arrays.asList(first, second));
        registry.unregisterBindings(Arrays.asList(first, second));

        assertEquals(1, first.getClosures());
        assertEquals(1, second.getClosures());
        assertTrue(registry.getBindings().isEmpty());
    }

    @Test
    public void ca02RemovesTheFirstBindingOfTheExactClass() {
        registry.registerBindings(first, second);
        registry.unregisterBinding(RecordingBinding.class);

        assertEquals(1, first.getClosures());
        assertEquals(0, second.getClosures());
        assertEquals(Collections.singletonList(second), registry.getBindings());
    }

    @Test
    public void ca02RejectsDuplicateRegistrationBeforeInitializingAgain() {
        registry.registerBinding(first);

        assertThrows(IllegalArgumentException.class, () -> registry.registerBinding(first));
        assertEquals(1, first.getInitializations());
    }

    @Test
    public void ca02RejectsRemovingAnUnregisteredBinding() {
        assertThrows(IllegalArgumentException.class, () -> registry.unregisterBinding(first));
        assertEquals(0, first.getClosures());
    }

    @Test
    public void ca02RejectsRemovingAnUnregisteredClass() {
        assertThrows(IllegalArgumentException.class,
          () -> registry.unregisterBinding(RecordingBinding.class));
    }

    @Test
    public void ca02ClosesAllBindingsAndLeavesAnEmptyRegistry() {
        registry.registerBindings(first, second);
        registry.unregisterAll();
        registry.unregisterAll();

        assertEquals(1, first.getClosures());
        assertEquals(1, second.getClosures());
        assertTrue(registry.getBindings().isEmpty());
    }

    @Test
    public void ca02PropagatesInitializationFailureWithoutRegisteringTheBinding() {
        IllegalStateException failure = new IllegalStateException("registration failed");
        FailingBinding binding = new FailingBinding(failure);

        assertSame(failure,
          assertThrows(IllegalStateException.class, () -> registry.registerBinding(binding)));
        assertTrue(registry.getBindings().isEmpty());
    }

    @Test
    public void ca02PropagatesDisposalFailureToTheCaller() {
        IllegalStateException failure = new IllegalStateException("disposal failed");
        FailingCloseBinding binding = new FailingCloseBinding(failure);
        registry.registerBinding(binding);

        assertSame(failure,
          assertThrows(IllegalStateException.class, () -> registry.unregisterBinding(binding)));
        assertTrue(registry.getBindings().isEmpty());
    }

    private static final class FailingBinding implements Binding<String> {
        private final IllegalStateException failure;

        private FailingBinding(IllegalStateException failure) {
            this.failure = failure;
        }

        @Override
        public void init(BindingContext context) {
            throw failure;
        }

        @Override
        public String get() {
            return "unregistered";
        }

        @Override
        public void close() {
        }
    }

    private static final class FailingCloseBinding implements Binding<String> {
        private final IllegalStateException failure;

        private FailingCloseBinding(IllegalStateException failure) {
            this.failure = failure;
        }

        @Override
        public void init(@NotNull BindingContext context) {
        }

        @Override
        public String get() {
            return "resource";
        }

        @Override
        public void close() {
            throw failure;
        }
    }
}
