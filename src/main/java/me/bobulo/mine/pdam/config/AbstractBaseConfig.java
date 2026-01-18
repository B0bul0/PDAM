package me.bobulo.mine.pdam.config;

import java.lang.reflect.Type;

public abstract class AbstractBaseConfig implements Config {

    public abstract Object getRaw(String key);

    @Override
    public String getString(String key) {
        Object raw = getRaw(key);
        return raw != null ? raw.toString() : null;
    }

    @Override
    public boolean getBoolean(String key) {
        Object raw = getRaw(key);
        return raw instanceof Boolean && (Boolean) raw;
    }

    @Override
    public int getInt(String key) {
        Object raw = getRaw(key);
        return raw instanceof Number ? ((Number) raw).intValue() : 0;
    }

    @Override
    public long getLong(String key) {
        Object raw = getRaw(key);
        return raw instanceof Number ? ((Number) raw).longValue() : 0;
    }

    @Override
    public float getFloat(String key) {
        Object raw = getRaw(key);
        return raw instanceof Number ? ((Number) raw).floatValue() : 0;
    }

    @Override
    public double getDouble(String key) {
        Object raw = getRaw(key);
        return raw instanceof Number ? ((Number) raw).doubleValue() : 0;
    }

    @Override
    public byte getByte(String key) {
        Object raw = getRaw(key);
        return raw instanceof Number ? ((Number) raw).byteValue() : 0;
    }

    @Override
    public short getShort(String key) {
        Object raw = getRaw(key);
        return raw instanceof Number ? ((Number) raw).shortValue() : 0;
    }

    @Override
    public <V> V getValue(String key, Class<V> valueType) {
        Object raw = getRaw(key);
        return valueType.isInstance(raw) ? valueType.cast(raw) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getValue(String key, Type valueType) {
        Object raw = getRaw(key);
        if (valueType instanceof Class<?>) {
            return getValue(key, (Class<V>) valueType);
        }

        try {
            return (V) raw;
        } catch (ClassCastException e) {
            return null;
        }
    }

}
