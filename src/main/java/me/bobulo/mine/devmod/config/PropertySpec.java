package me.bobulo.mine.devmod.config;

import java.util.function.Consumer;

public class PropertySpec<T> {

    public static <T> PropertySpec<T> create(String name, T defaultValue) {
        return new PropertySpec<>(name, defaultValue);
    }

    private final String name;
    private T defaultValue;
    private String comment;
    private Number minValue;
    private Number maxValue;
    private Consumer<T> onUpdateCallback;

    private PropertySpec(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public PropertySpec<T> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public PropertySpec<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    public PropertySpec<T> min(Number minValue) {
        this.minValue = minValue;
        return this;
    }

    public PropertySpec<T> max(Number maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public PropertySpec<T> onUpdate(Consumer<T> callback) {
        this.onUpdateCallback = callback;
        return this;
    }

    /* Getters */

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public Number getMinValue() {
        return minValue;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    public Consumer<T> getOnUpdateCallback() {
        return onUpdateCallback;
    }

    public Type getType() {
        if (defaultValue instanceof Boolean) {
            return Type.BOOLEAN;
        } else if (defaultValue instanceof Integer) {
            return Type.INTEGER;
        } else if (defaultValue instanceof Double) {
            return Type.DOUBLE;
        } else if (defaultValue instanceof String) {
            return Type.STRING;
        } else if (defaultValue instanceof boolean[]) {
            return Type.BOOLEAN_ARRAY;
        } else if (defaultValue instanceof int[]) {
            return Type.INTEGER_ARRAY;
        } else if (defaultValue instanceof double[]) {
            return Type.DOUBLE_ARRAY;
        } else if (defaultValue instanceof String[]) {
            return Type.STRING_ARRAY;
        } else {
            throw new IllegalStateException("Unsupported property type: " + defaultValue.getClass().getName());
        }
    }

    public enum Type {
        BOOLEAN,
        INTEGER,
        DOUBLE,
        STRING,
        BOOLEAN_ARRAY,
        INTEGER_ARRAY,
        DOUBLE_ARRAY,
        STRING_ARRAY
    }

}
