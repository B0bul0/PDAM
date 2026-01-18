package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.PDAM;
import org.jetbrains.annotations.NotNull;

public final class ConfigElement<V> implements ConfigValue<V> {

    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull V defaultValue) {
        return of(key, defaultValue, PDAM.getConfigService().getMainConfig());
    }

    @SuppressWarnings("unchecked")
    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull V defaultValue, @NotNull Config config) {
        ConfigElement<V> element = new ConfigElement<>();
        element.key = key;
        element.valueType = (Class<V>) defaultValue.getClass();
        element.defaultValue = defaultValue;
        element.config = config;
        return element;
    }

    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull Class<V> valueType) {
        return of(key, valueType, PDAM.getConfigService().getMainConfig());
    }

    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull Class<V> valueType, @NotNull Config config) {
        ConfigElement<V> element = new ConfigElement<>();
        element.key = key;
        element.valueType = valueType;
        element.config = config;
        return element;
    }

    private String key;
    private Class<V> valueType;
    private V defaultValue;
    private Config config;

    private ConfigElement() {
    }

    @Override
    public V get() {
        V value = config.getValue(key, valueType);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(V value) {
        config.setValue(key, value);
    }
}
