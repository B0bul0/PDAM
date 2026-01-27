package me.bobulo.mine.pdam.attribute;

import java.lang.reflect.Type;
import java.util.Objects;

public final class AttributeKey<T> {

    public static <T> AttributeKey<T> of(String name) {
        return new AttributeKey<>(name);
    }

    public static <T> AttributeKey<T> of(String name, Type type) {
        return new AttributeKey<>(name, type);
    }

    public static <T> AttributeKey<T> of(String name, Class<T> typeClass) {
        return new AttributeKey<>(name, typeClass);
    }

    private final String name;
    private final Type type;

    private AttributeKey(String name) {
        this.name = name;
        this.type = null;
    }

    private AttributeKey(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeKey<?> that = (AttributeKey<?>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}