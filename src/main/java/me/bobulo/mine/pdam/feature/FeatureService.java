package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.util.ThreadUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Manages the registration and lifecycle of features within the mod.
 * This service acts as a central registry for all {@link Feature} instances.
 */
public final class FeatureService {

    private static final Logger log = LogManager.getLogger(FeatureService.class);

    private final Map<String, Feature> features = new ConcurrentHashMap<>();

    /**
     * Initializes and registers a feature, e.g. {@code registerFeature(new SoundDebugFeature())}.
     * Failed initialization releases its scopes and leaves no registration behind.
     *
     * @param feature The feature to register. Must not be null, and its ID must not be blank.
     * @throws IllegalArgumentException if a feature with the same ID is already registered.
     */
    public void registerFeature(Feature feature) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be registered from the main thread");
        Validate.notNull(feature, "Feature cannot be null");
        Validate.notBlank(feature.getId(), "Feature id cannot be null");
        Validate.isTrue(!features.containsKey(feature.getId()), "Feature with id " + feature.getId() + " is already registered");

        feature.initialize();
        features.put(feature.getId(), feature);
        log.debug("Registered feature: {}", feature.getId());
    }

    /**
     * Disables a feature, detaches its modules and cleans up its lifetime scope before removal.
     * For example, {@code service.unregisterFeature(id)} ends both scopes.
     * An unknown identifier has no effect.
     */
    public void unregisterFeature(@NotNull String id) {
        Feature feature = features.get(id);
        if (feature != null) {
            Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be removed from the main thread");
            try {
                feature.uninitialize();
            } finally {
                features.remove(id);
            }
        }
    }

    /**
     * Removes all features with their resources, for example {@code service.unregisterAll()}.
     */
    public void unregisterAll() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be removed from the main thread");
        RuntimeException failure = null;
        for (Feature feature : features.values()) {
            try {
                unregisterFeature(feature.getId());
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else if (failure != exception) {
                    failure.addSuppressed(exception);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
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
        List<Feature> sorted = new ArrayList<>(features.values());
        sorted.sort(Comparator.comparing(Feature::getId));
        return sorted;
    }

    /**
     * Retrieves all behaviors of a specific class from all registered features.
     *
     * @param behaviorClass The class of the behavior to retrieve.
     * @return A list of all behaviors of the specified class from all registered features.
     */
    public <T extends FeatureBehavior> List<T> getAllBehaviors(@NotNull Class<T> behaviorClass) {
        List<T> behaviors = new ArrayList<>();
        for (Feature feature : features.values()) {
            feature.getLoadScope().getBehaviorRegistry().collectBehaviors(behaviorClass, behaviors);
            if (feature.getEnableScope() != null) {
                feature.getEnableScope().getBehaviorRegistry().collectBehaviors(behaviorClass, behaviors);
            }
        }
        return behaviors;
    }

}
