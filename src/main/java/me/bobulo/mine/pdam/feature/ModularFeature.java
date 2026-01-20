package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.module.FeatureModule;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * An interface representing a feature that can have modules added to it.
 */
public interface ModularFeature {

    /**
     * Adds a module to this feature.
     *
     * @param module The module to add to this feature.
     * @throws IllegalArgumentException if running asynchronously the minecraft main thread.
     * @throws IllegalArgumentException if the module is already added to this feature.
     * @throws NullPointerException     if the module is null.
     */
    void addModule(@NotNull FeatureModule module);

    /**
     * Removes a module from this feature.
     *
     * @param module The module to remove from this feature.
     * @throws IllegalArgumentException if running asynchronously the minecraft main thread.
     * @throws IllegalArgumentException if the module is not added to this feature.
     * @throws NullPointerException if the module is null.
     */
    void removeModule(@NotNull FeatureModule module);

    /**
     * Retrieves all modules added to this feature.
     *
     * @return A collection of all modules added to this feature.
     */
    @NotNull Collection<FeatureModule> getModules();

    /**
     * Retrieves a behavior of the specified class.
     *
     * @param behaviorClass The behavior class to retrieve.
     * @param <T> The type of the behavior.
     * @return The behavior instance of the specified class.
     */
    <T extends FeatureBehavior> T getBehavior(@NotNull Class<T> behaviorClass);

    /**
     * Checks if the feature has a behavior of the specified class.
     *
     * @param behaviorClass The behavior class to check.
     * @param <T> The type of the behavior.
     * @return True if the feature has the specified behavior, false otherwise.
     */
    <T extends FeatureBehavior> boolean hasBehavior(@NotNull Class<T> behaviorClass);

    /**
     * Retrieves all behaviors in this feature.
     *
     * @param <T> The type of the behavior.
     * @return A collection of all behaviors in this feature.
     */
    <T extends FeatureBehavior> Collection<T> getBehaviors();

    /**
     * Retrieves all instances of the specified behavior class in this feature.
     *
     * @param behaviorClass The behavior class to retrieve.
     * @param <T> The type of the behavior.
     * @return A list of all behaviors of the specified class in this feature.
     */
    <T extends FeatureBehavior> List<T> getBehaviors(@NotNull Class<T> behaviorClass);

}
