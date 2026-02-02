package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.module.FeatureModule;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages the registration and lifecycle of features within the mod.
 * This service acts as a central registry for all {@link Feature} instances.
 */
public final class FeatureService {

    private static final Logger log = LogManager.getLogger(FeatureService.class);

    private final Map<String, Feature> features = new ConcurrentHashMap<>();

    /**
     * Registers a new feature with the service.
     *
     * @param feature The feature to register. Must not be null, and its ID must not be blank.
     * @throws IllegalArgumentException if a feature with the same ID is already registered.
     */
    public void registerFeature(Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        Validate.notBlank(feature.getId(), "Feature id cannot be null");
        Validate.isTrue(!features.containsKey(feature.getId()), "Feature with id " + feature.getId() + " is already registered");

        features.put(feature.getId(), feature);
        log.debug("Registered feature: {}", feature.getId());
    }

    /**
     * Retrieves a registered feature by its unique ID.
     *
     * @param id The ID of the feature to retrieve.
     * @return The {@link Feature} instance, or {@code null} if no feature with that ID is found.
     */
    public Feature getFeature(String id) {
        return features.get(id);
    }

    /**
     * Checks if a feature with the given ID is registered.
     *
     * @param id The ID of the feature to check.
     * @return {@code true} if the feature is registered, {@code false} otherwise.
     */
    public boolean isFeatureRegistered(String id) {
        return features.containsKey(id);
    }

    /**
     * Checks if a feature with the given ID is enabled.
     *
     * @param id The ID of the feature to check.
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     */
    public boolean isFeatureEnabled(String id) {
        Feature feature = getFeature(id);
        return feature != null && feature.isEnabled();
    }

    /**
     * Gets a list of all registered features, sorted alphabetically by their ID.
     *
     * @return A sorted list of {@link Feature} instances.
     */
    public List<Feature> getSortedFeatures() {
        return features.values().stream()
          .sorted(Comparator.comparing(Feature::getId))
          .collect(Collectors.toList());
    }

    /**
     * Retrieves all behaviors of a specific class from all registered features.
     *
     * @param behaviorClass The class of the behavior to retrieve.
     * @return A list of all behaviors of the specified class from all registered features.
     */
    public <T extends FeatureBehavior> List<T> getAllBehaviors(@NotNull Class<T> behaviorClass) {
        return features.values().stream()
          .flatMap(feature -> feature.getBehaviors(behaviorClass).stream())
          .collect(Collectors.toList());
    }

}