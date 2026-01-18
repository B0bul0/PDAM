package me.bobulo.mine.pdam.config;

/**
 * A configuration value that can be retrieved and updated.
 *
 * @param <V> the type of the configuration value
 */
public interface ConfigValue<V> {

    /**
     * Gets the current value of the configuration.
     *
     * @return the current value of the configuration
     */
    V get();

    /**
     * Gets the current value of the configuration, or returns the specified default
     * value if the current value is null.
     *
     * @param defaultValue the default value to return if the current value is null
     * @return the current value of the configuration or defaultValue if null
     */
    default V getOrDefault(V defaultValue) {
        return get() != null ? get() : defaultValue;
    }

    /**
     * Sets a new value for the configuration.
     * If null is provided, it may reset to a default state depending on implementation.
     *
     * @param value the new value to set
     */
    void set(V value);

}
