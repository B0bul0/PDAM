package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.PDAM;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A configuration property that can get and set values in a configuration source.
 *
 * @param <V> the type of the configuration value
 */
public final class ConfigProperty<V> implements ConfigValue<V> {

    /* Static factory methods */

    public static <V> ConfigProperty<V> of(@NotNull String key, @NotNull Class<V> valueType) {
        return new ConfigProperty<>(key, valueType, () -> null, PDAM.getConfigService().getMainConfig(), null);
    }

    @SuppressWarnings("unchecked")
    public static <V> ConfigProperty<V> of(@NotNull String key, @NotNull V defaultValue) {
        return new ConfigProperty<>(key, (Class<V>) defaultValue.getClass(), () -> defaultValue, PDAM.getConfigService().getMainConfig(), null);
    }

    public static ConfigProperty<String> ofString(@NotNull String key) {
        return of(key, String.class);
    }

    public static ConfigProperty<Boolean> ofBoolean(@NotNull String key) {
        return of(key, Boolean.class);
    }

    public static ConfigProperty<Integer> ofInt(@NotNull String key) {
        return of(key, Integer.class);
    }

    public static ConfigProperty<Long> ofLong(@NotNull String key) {
        return of(key, Long.class);
    }

    public static ConfigProperty<Double> ofDouble(@NotNull String key) {
        return of(key, Double.class);
    }

    public static ConfigProperty<Float> ofFloat(@NotNull String key) {
        return of(key, Float.class);
    }

    public static ConfigProperty<Short> ofShort(@NotNull String key) {
        return of(key, Short.class);
    }

    public static ConfigProperty<Byte> ofByte(@NotNull String key) {
        return of(key, Byte.class);
    }

    public static ConfigProperty<Character> ofChar(@NotNull String key) {
        return of(key, Character.class);
    }

    /* Instance fields and methods */

    private final String key;
    private final Class<V> valueType;
    private final Supplier<V> defaultValue;
    private final Config config;
    private final Consumer<V> onChange;

    private ConfigProperty(String key, Class<V> valueType, Supplier<V> defaultValue, Config config, Consumer<V> onChange) {
        this.key = key;
        this.valueType = valueType;
        this.defaultValue = defaultValue;
        this.config = config;
        this.onChange = onChange;
    }

    @Override
    public V get() {
        V value = config.getValue(key, valueType);
        return value != null ? value : defaultValue.get();
    }

    @Override
    public void set(V value) {
        V oldValue = get();
        config.setValue(key, value);

        if (config instanceof PersistentConfig) {
            ((PersistentConfig) config).saveConfig();
        }

        if (onChange != null && !Objects.equals(oldValue, value)) {
            onChange.accept(value);
        }
    }

    /**
     * Synchronizes the current value by invoking the onChange callback with the current value.
     */
    public ConfigProperty<V> sync() {
        if (onChange != null) {
            onChange.accept(get());
        }
        return this;
    }

    /* Fluent builders */

    public ConfigProperty<V> withDefault(V defaultValue) {
        return new ConfigProperty<>(key, valueType, () -> defaultValue, config, onChange);
    }

    public ConfigProperty<V> withDefault(@NotNull Supplier<V> defaultValue) {
        return new ConfigProperty<>(key, valueType, defaultValue, config, onChange);
    }

    public ConfigProperty<V> withConfig(@NotNull Config config) {
        return new ConfigProperty<>(key, valueType, defaultValue, config, onChange);
    }

    public ConfigProperty<V> onChange(@NotNull Consumer<V> onChange) {
        return new ConfigProperty<>(key, valueType, defaultValue, config, onChange);
    }
}