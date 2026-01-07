package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigBinder;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

        ConfigBinder configBinder = new ConfigBinder(feature.getId());
        PDAM.getConfigService().registerBinder(configBinder);
        configBinder.initialize(feature);
    }

    /**
     * Retrieves a registered feature by its unique ID.
     *
     * @param name The ID of the feature to retrieve.
     * @return The {@link Feature} instance, or {@code null} if no feature with that ID is found.
     */
    public Feature getFeature(String name) {
        return features.get(name);
    }

    /**
     * Checks if a feature with the given ID is registered.
     *
     * @param name The ID of the feature to check.
     * @return {@code true} if the feature is registered, {@code false} otherwise.
     */
    public boolean isFeatureRegistered(String name) {
        return features.containsKey(name);
    }

    /**
     * Checks if a feature with the given ID is enabled.
     *
     * @param name The ID of the feature to check.
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     */
    public boolean isFeatureEnabled(String name) {
        Feature feature = getFeature(name);
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

}