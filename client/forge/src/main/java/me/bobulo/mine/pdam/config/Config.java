package me.bobulo.mine.pdam.config;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * Interface representing a configuration source.
 * <p>
 * Configuration retrieval methods return null if the configuration does not exist.
 * If it exists but is of a different type, the method attempts to adapt it or returns null.
 */
public interface Config {

    /**
     * Gets the string value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a string, returns null.
     *
     * @param key the key
     * @return the string value associated with the key
     */
    String getString(String key);

    /**
     * Gets the boolean value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a boolean, returns false.
     *
     * @param key the key
     * @return the boolean value associated with the key or false if not found
     */
    boolean getBoolean(String key);

    /**
     * Gets the int value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to an int, returns 0.
     *
     * @param key the key
     * @return the int value associated with the key or 0 if not found
     */
    int getInt(String key);

    /**
     * Gets the long value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a long, returns 0.
     *
     * @param key the key
     * @return the long value associated with the key or 0 if not found
     */
    long getLong(String key);

    /**
     * Gets the float value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a float, returns 0.
     *
     * @param key the key
     * @return the float value associated with the key or 0 if not found
     */
    float getFloat(String key);

    /**
     * Gets the double value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a double, returns 0.
     *
     * @param key the key
     * @return the double value associated with the key or 0 if not found
     */
    double getDouble(String key);

    /**
     * Gets the byte value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a byte, returns 0.
     *
     * @param key the key
     * @return the byte value associated with the key or 0 if not found
     */
    byte getByte(String key);

    /**
     * Gets the short value associated with the specified key.
     * <p>
     * If the value does not exist or cannot be converted to a short, returns 0.
     *
     * @param key the key
     * @return the short value associated with the key or 0 if not found
     */
    short getShort(String key);

    /**
     * Gets the value associated with the specified key, converted to the specified type.
     * <p>
     * If the value does not exist or cannot be converted to the specified type, returns null.
     *
     * @param key       the key
     * @param valueType the class of the value type
     * @param <V>       the type of the value
     * @return the value associated with the key or null if not found or cannot be converted
     */
    <V> V getValue(String key, @NotNull Class<V> valueType);

    /**
     * Gets the value associated with the specified key, converted to the specified type.
     * <p>
     * If the value does not exist or cannot be converted to the specified type, returns null.
     *
     * @param key       the key
     * @param valueType the type of the value
     * @param <V>       the type of the value
     * @return the value associated with the key or null if not found or cannot be converted
     */
    <V> V getValue(String key, @NotNull Type valueType);

    /**
     * Sets the value associated with the specified key.
     *
     * @param key   the key
     * @param value the value to set
     */
    void setValue(String key, Object value);

}
