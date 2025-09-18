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

public class FeatureService {

    private static final Logger log = LogManager.getLogger(FeatureService.class);

    private final Map<String, Feature> features = new ConcurrentHashMap<>();

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

    public Feature getFeature(String name) {
        return features.get(name);
    }

    public List<Feature> getSortedFeatures() {
        return features.values().stream()
          .sorted(Comparator.comparing(Feature::getId))
          .collect(Collectors.toList());
    }

}
