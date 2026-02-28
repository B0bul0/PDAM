package me.bobulo.mine.pdam.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an object that can store and retrieve typed attributes dynamically.
 */
public interface Attributable {

    /**
     * Sets a value for the specified attribute key.
     *
     * @param key   The typed key to associate the value with.
     * @param value The value to be stored.
     * @param <T>   The type of the value.
     */
    <T> void set(@NotNull AttributeKey<T> key, T value);

    /**
     * Retrieves the value associated with the specified attribute key.
     *
     * @param key The typed key to retrieve the value for.
     * @param <T> The type of the value.
     * @return The value associated with the key, or null if not present.
     */
    @Nullable
    <T> T get(@NotNull AttributeKey<T> key);

    /**
     * Checks if a value is present for the specified attribute key.
     *
     * @param key The typed key to check.
     * @param <T> The type of the value.
     * @return true if the attribute exists, false otherwise.
     */
    <T> boolean has(@NotNull AttributeKey<T> key);

    /**
     * Removes the value associated with the specified attribute key.
     *
     * @param key The typed key to remove.
     * @param <T> The type of the value.
     */
    <T> void remove(@NotNull AttributeKey<T> key);
}