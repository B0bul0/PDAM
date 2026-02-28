package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.attribute.Attributable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a modular feature of the mod.
 * Each feature can be enabled or disabled.
 */
public interface Feature extends ModularFeature, Attributable {

    /**
     * Gets the unique identifier for this feature.
     * This ID is used for configuration and internal management.
     *
     * @return The unique ID of the feature.
     */
    @NotNull
    String getId();

    /**
     * Gets the display name of the feature.
     * By default, it returns the feature's ID.
     *
     * @return The display name of the feature.
     */
    @NotNull
    default String getName() {
        return getId();
    }

    /**
     * Gets the description of the feature.
     * By default, it returns an empty string.
     *
     * @return The description of the feature.
     */
    @NotNull
    default String getDescription() {
        return "";
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
     * such as registering event listeners or modules.
     */
    void enable();

    /**
     * Disables the feature.
     * This method should handle the logic to deactivate the feature,
     * ensuring it no longer affects the game.
     */
    void disable();

    /**
     * Toggles the enabled state of the feature.
     */
    default void toggleEnabled() {
        if (isEnabled()) {
            disable();
        } else {
            enable();
        }
    }
}