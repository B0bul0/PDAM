package me.bobulo.mine.pdam.config;

public interface ConfigValue<V> {

    V get();

    default V getOrDefault(V defaultValue) {
        return get() != null ? get() : defaultValue;
    }

    void set(V value);

}
