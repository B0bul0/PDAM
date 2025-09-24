package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.config.PropertyDeclarer;

/**
 * Represents a modular feature of the mod.
 * Each feature can be enabled or disabled and may have its own configuration properties.
 */
public interface Feature extends PropertyDeclarer {

    /**
     * Gets the unique identifier for this feature.
     * This ID is used for configuration and internal management.
     *
     * @return The unique ID of the feature.
     */
    String getId();

    /**
     * Gets the display name of the feature.
     * By default, it returns the feature's ID.
     *
     * @return The display name of the feature.
     */
    default String getName() {
        return getId();
    }

    /**
     * Checks if the feature is currently enabled.
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     */
    boolean isEnabled();

    /**
     * Enables the feature.
     * This method should handle the logic to activate the feature,
     * such as registering event listeners or components.
     */
    void enable();

    /**
     * Disables the feature.
     * This method should handle the logic to deactivate the feature,
     * ensuring it no longer affects the game.
     */
    void disable();

}