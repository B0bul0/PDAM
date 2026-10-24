package me.bobulo.mine.pdam.binding;

import me.bobulo.mine.pdam.binding.context.BindingContext;
import me.bobulo.mine.pdam.scope.Scope;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoundValuesTest {
    private final Scope scope = new Scope();

    @Test
    void ca10GetsValuesFromDifferentBindingsByAssignableType() {
        scope.registerBindings(new ValueBinding("first"), new ValueBinding(12), new ValueBinding("second"));
        assertEquals("first", scope.get(CharSequence.class));
        assertEquals(Arrays.asList("first", "second"), scope.gets(CharSequence.class));
        assertEquals(12, scope.get(Number.class));
        assertEquals(3, scope.gets(Object.class).size());
    }

    @Test
    void ca10MissingTypesAndNullValuesHaveNoMatches() {
        scope.registerBinding(new ValueBinding(null));
        assertNull(scope.get(String.class));
        assertTrue(scope.gets(String.class).isEmpty());
        assertThrows(NullPointerException.class, () -> scope.get(null));
        assertThrows(NullPointerException.class, () -> scope.gets(null));
    }

    @Test
    void ca10RemovedBindingsStopSupplyingValues() {
        ValueBinding first = new ValueBinding("first");
        scope.registerBindings(first, new ValueBinding("second"));
        scope.unregisterBinding(first);
        assertEquals("second", scope.get(String.class));
        scope.cleanup();
        assertTrue(scope.gets(Object.class).isEmpty());
    }

    @Test
    void ca11CleanupClosesEveryBindingAndRetainsSuppressedFailures() {
        IllegalStateException firstFailure = new IllegalStateException("first failure");
        IllegalStateException secondFailure = new IllegalStateException("second failure");
        ValueBinding first = new ValueBinding("first", firstFailure);
        ValueBinding second = new ValueBinding("second", secondFailure);
        ValueBinding third = new ValueBinding("third");
        scope.registerBindings(first, second, third);
        assertSame(firstFailure, assertThrows(IllegalStateException.class, scope::cleanup));
        assertArrayEquals(new Throwable[]{secondFailure}, firstFailure.getSuppressed());
        assertEquals(1, third.closures);
        assertTrue(scope.getBindings().isEmpty());
        scope.cleanup();
        assertEquals(1, first.closures);
    }

    @Test
    void ca10DistinctEqualBindingsAreRegisteredAndRemovedByIdentity() {
        EqualBinding first = new EqualBinding();
        EqualBinding second = new EqualBinding();
        scope.registerBindings(first, second);
        assertEquals(2, scope.gets(String.class).size());
        scope.unregisterBinding(second);
        assertSame(first, scope.getBindings().get(0));
        assertEquals(1, scope.getBindings().size());
    }

    private static final class EqualBinding extends ValueBinding {
        EqualBinding() {
            super("equal");
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof EqualBinding;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    private static class ValueBinding implements Binding<Object> {
        private final Object value;
        private final RuntimeException failure;
        private int closures;

        ValueBinding(Object value) {
            this(value, null);
        }

        ValueBinding(Object value, RuntimeException failure) {
            this.value = value;
            this.failure = failure;
        }

        @Override
        public void init(BindingContext context) {
        }

        @Override
        public Object get() {
            return value;
        }

        @Override
        public void close() {
            closures++;
            if (failure != null) {
                throw failure;
            }
        }
    }
}
