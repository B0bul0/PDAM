package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.config.ConfigBinder;
import me.bobulo.mine.pdam.config.ConfigInitContext;
import me.bobulo.mine.pdam.feature.component.FeatureComponent;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of a Feature.
 */
public class FeatureImpl implements Feature, ComponentableFeature {

    private static final Logger log = LogManager.getLogger(FeatureImpl.class);

    private final String id;
    private final String name;
    private boolean enabled = false;

    private final List<FeatureComponent> components = new ArrayList<>();

    private ConfigBinder configBinder;

    public FeatureImpl(String id) {
        Validate.notBlank(id, "Feature id cannot be null or blank");
        this.id = id;
        this.name = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void initProperties(ConfigInitContext context) {
        this.configBinder = context.getConfigBinder();

        context.createProperty("enabled", false)
          .comment("Enable or disable the " + name + " feature")
          .onUpdate(newVal -> {
              if (newVal) {
                  enable();
              } else {
                  disable();
              }
          });

        for (FeatureComponent component : components) {
            component.initProperties(context);
        }
    }

    @Override
    public void enable() {
        if (!enabled) {
            log.info("Enabling feature {}", id);

            onEnable();
            enabled = true;

            log.info("Feature {} enabled", id);
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            log.info("Disabling feature {}", id);

            onDisable();
            enabled = false;

            log.info("Feature {} disabled", id);
        }
    }

    private void onEnable() {
        for (FeatureComponent component : components) {
            component.enable();
        }
    }

    private void onDisable() {
        for (FeatureComponent component : components) {
            component.disable();
        }
    }

    /* Register Components */

    @Override
    public void addComponent(@NotNull FeatureComponent component) {
        component.init(this);
        components.add(component);

        if (configBinder != null) {
            ConfigInitContext context = new ConfigInitContext(configBinder);
            component.initProperties(context);
            configBinder.initialize(component);
        }

        if (enabled) {
            component.enable();
        }
    }

    @Override
    public void removeComponent(@NotNull FeatureComponent component) {
        components.remove(component);

        if (enabled) {
            component.disable();
        }
    }

    @Override
    public boolean hasComponent(@NotNull FeatureComponent component) {
        return components.contains(component);
    }

    @Override
    public List<FeatureComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    /* Builder */

    public static FeatureBuilder builder() {
        return new FeatureBuilder();
    }

    public static final class FeatureBuilder {
        private String id;
        private final List<FeatureComponent> components = new ArrayList<>();

        private FeatureBuilder() {
        }

        public FeatureBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FeatureBuilder components(List<FeatureComponent> components) {
            this.components.addAll(components);
            return this;
        }

        public FeatureBuilder components(FeatureComponent... components) {
            this.components.addAll(Arrays.asList(components));
            return this;
        }

        public FeatureBuilder component(FeatureComponent component) {
            this.components.add(component);
            return this;
        }

        public FeatureImpl build() {
            FeatureImpl feature = new FeatureImpl(id);
            this.components.forEach(feature::addComponent);
            return feature;
        }
    }
}
