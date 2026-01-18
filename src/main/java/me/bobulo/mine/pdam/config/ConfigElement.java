package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.PDAM;
import org.jetbrains.annotations.NotNull;

public final class ConfigElement<V> implements ConfigValue<V> {

    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull V defaultValue) {
        return of(key, defaultValue, PDAM.getConfigService().getMainConfig());
    }

    @SuppressWarnings("unchecked")
    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull V defaultValue, @NotNull Config config) {
        return new ConfigElement<>(key, (Class<V>) defaultValue.getClass(), defaultValue, config);
    }

    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull Class<V> valueType) {
        return of(key, valueType, PDAM.getConfigService().getMainConfig());
    }

    public static <V> ConfigElement<V> of(@NotNull String key, @NotNull Class<V> valueType, @NotNull Config config) {
        return new ConfigElement<>(key, valueType, null, config);
    }

    private final String key;
    private final Class<V> valueType;
    private final V defaultValue;
    private final Config config;

    private ConfigElement(String key, Class<V> valueType, V defaultValue, Config config) {
        this.key = key;
        this.valueType = valueType;
        this.defaultValue = defaultValue;
        this.config = config;
    }

    @Override
    public V get() {
        V value = config.getValue(key, valueType);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(V value) {
        config.setValue(key, value);

        if (config instanceof PersistentConfig) { // TODO improve
            ((PersistentConfig) config).saveConfig();
        }
    }
}
