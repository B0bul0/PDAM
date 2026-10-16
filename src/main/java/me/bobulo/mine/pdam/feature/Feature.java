package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.attribute.Attributable;
import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a modular feature of the mod.
 * Modules use onAttach/onDetach for their lifetime and enable/disable for activation.
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
     * Returns the lifetime scope, available before modules attach and preserved while disabled.
     * For example, register persistent bindings in {@code feature.getLoadScope()} during onAttach.
     * The service cleans it up after detaching the modules when removing this feature.
     */
    @NotNull Scope getLoadScope();

    /**
     * Returns the scope of the current activation, or {@code null} while disabled.
     * For example, register activation bindings in {@code feature.getEnableScope()} during enable.
     */
    @Nullable Scope getEnableScope();

    /**
     * Checks if the feature is currently enabled.
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     */
    boolean isEnabled();

    /**
     * Enables the feature.
     * Creates a new enable scope.
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
