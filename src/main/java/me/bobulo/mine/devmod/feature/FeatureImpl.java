package me.bobulo.mine.devmod.feature;

import me.bobulo.mine.devmod.config.ConfigInitContext;
import me.bobulo.mine.devmod.feature.component.FeatureComponent;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureImpl implements Feature {

    private static final Logger log = LogManager.getLogger(FeatureImpl.class);

    private final String id;
    private final String name;
    private boolean enabled = false;

    private final List<FeatureComponent> components = new ArrayList<>();

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
        context.createProperty("enabled", false)
          .comment("Enable or disable the " + name + " feature")
          .onUpdate((newVal) -> {
              if (newVal) {
                  enable();
              } else {
                  disable();
              }
          });
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

    public void addComponent(FeatureComponent component) {
        component.init(this);
        components.add(component);

        if (enabled) {
            component.enable();
        }
    }

    public void removeComponent(FeatureComponent component) {
        components.remove(component);

        if (enabled) {
            component.disable();
        }
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
