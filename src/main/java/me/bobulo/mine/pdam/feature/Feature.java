package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.attribute.Attributable;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.scope.Scope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A feature owns scopes for unique modules, repeatable behaviors and lifecycle bindings.
 * Load registrations persist while disabled; enable registrations last for one activation.
 */
public interface Feature extends Attributable {

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
    Scope getEnableScope();

    /**
     * Returns a snapshot of assignable bound values from load, then the current enable scope.
     * For example, {@code feature.getBoundValues(Listener.class)} retrieves registered listeners.
     */
    default <T> List<T> getBoundValues(@NotNull Class<T> type) {
        List<T> result = getLoadScope().gets(type);
        Scope enableScope = getEnableScope();
        if (enableScope != null) {
            result.addAll(enableScope.gets(type));
        }
        return result;
    }

    /**
     * Checks if the feature is currently enabled.
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     */
    boolean isEnabled();

    /**
     * Initializes the feature's persistent resources before registration completes.
     * Repeated initialization has no effect; for example, the service calls this on registration.
     */
    void initialize();

    /**
     * Disables the feature and releases its hooks and scopes, e.g. when the service removes it.
     */
    void uninitialize();

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

    /**
     * Adds a lifetime module, e.g. feature.addModule(module). Its concrete type must be unique.
     */
    default void addModule(@NotNull FeatureModule module) {
        getLoadScope().registerModule(module);
    }

    /**
     * Removes a registered module from its owning scope.
     */
    default void removeModule(@NotNull FeatureModule module) {
        Scope owner = getLoadScope().getModule(module.getClass()) == module ? getLoadScope() : getEnableScope();
        if (owner == null) {
            throw new IllegalArgumentException("Module not registered: " + module);
        }
        owner.unregisterModule(module);
    }

    /**
     * Retrieves one exact module type in constant time, e.g. feature.getModule(MyModule.class).
     */
    default <T extends FeatureModule> T getModule(@NotNull Class<T> type) {
        T module = getLoadScope().getModule(type);
        return module != null || getEnableScope() == null ? module : getEnableScope().getModule(type);
    }

    /**
     * Returns modules from both current scopes for inspection.
     */
    default Collection<FeatureModule> getModules() {
        List<FeatureModule> result = new ArrayList<>();
        result.addAll(getLoadScope().getModuleRegistry().getModules());
        if (getEnableScope() != null) {
            result.addAll(getEnableScope().getModuleRegistry().getModules());
        }
        return result;
    }

    /**
     * Adds a repeatable lifetime behavior, e.g. feature.addBehavior(behavior).
     */
    default void addBehavior(@NotNull FeatureBehavior behavior) {
        getLoadScope().registerBehavior(behavior);
    }

    /**
     * Removes an exact behavior instance from its owning scope.
     */
    default void removeBehavior(@NotNull FeatureBehavior behavior) {
        if (getLoadScope().getBehaviorRegistry().contains(behavior)) {
            getLoadScope().unregisterBehavior(behavior);
        } else if (getEnableScope() != null) {
            getEnableScope().unregisterBehavior(behavior);
        } else {
            throw new IllegalArgumentException("Behavior not registered: " + behavior);
        }
    }

    /**
     * Returns all matching behaviors, e.g. feature.getBehaviors(MenuImGuiRender.class).
     */
    default <T extends FeatureBehavior> List<T> getBehaviors(@NotNull Class<T> type) {
        List<T> result = new ArrayList<>();
        getLoadScope().getBehaviorRegistry().collectBehaviors(type, result);
        if (getEnableScope() != null) {
            getEnableScope().getBehaviorRegistry().collectBehaviors(type, result);
        }
        return result;
    }

    /**
     * Whether either scope contains a behavior assignable to this type.
     */
    default boolean hasBehavior(@NotNull Class<? extends FeatureBehavior> type) {
        for (FeatureBehavior behavior : getLoadScope().getBehaviorRegistry().getBehaviors()) {
            if (type.isInstance(behavior)) {
                return true;
            }
        }
        if (getEnableScope() != null) {
            for (FeatureBehavior behavior : getEnableScope().getBehaviorRegistry().getBehaviors()) {
                if (type.isInstance(behavior)) {
                    return true;
                }
            }
        }
        return false;
    }
}
