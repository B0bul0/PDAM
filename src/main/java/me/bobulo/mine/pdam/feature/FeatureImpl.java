package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.component.FeatureComponent;
import me.bobulo.mine.pdam.feature.event.FeatureComponentDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureComponentEnabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Default implementation of a Feature.
 */
public final class FeatureImpl implements Feature, ComponentableFeature {

    private static final Logger log = LogManager.getLogger(FeatureImpl.class);

    private final String id;
    private final String name;
    private boolean enabled = false;

    private final Map<Class<? extends FeatureComponent>, FeatureComponent> components = new HashMap<>();

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
    public void enable() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be enabled from the main thread");

        if (!enabled) {
            log.info("Enabling feature {}", id);

            onEnable();
            enabled = true;

            EventUtils.callEvent(new FeatureEnabledEvent(this));

            log.info("Feature {} enabled", id);
        }
    }

    @Override
    public void disable() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be disabled from the main thread");

        if (enabled) {
            log.info("Disabling feature {}", id);

            onDisable();
            enabled = false;

            EventUtils.callEvent(new FeatureDisabledEvent(this));

            log.info("Feature {} disabled", id);
        }
    }

    private void onEnable() {
        for (FeatureComponent component : components.values()) {
            try {
                component.enable();
                EventUtils.callEvent(new FeatureComponentEnabledEvent(this, component));
            } catch (Exception exception) {
                log.error("Error while enabling component {} of feature {}",
                  component.getClass().getSimpleName(), id, exception);
            }
        }
    }

    private void onDisable() {
        for (FeatureComponent component : components.values()) {
            try {
                component.disable();
                EventUtils.callEvent(new FeatureComponentDisabledEvent(this, component));
            } catch (Exception exception) {
                log.error("Error while disabling component {} of feature {}",
                  component.getClass().getSimpleName(), id, exception);
            }
        }
    }

    /* Register Components */

    @Override
    public void addComponent(@NotNull FeatureComponent component) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Components can only be added from the main thread");
        Validate.isTrue(!components.containsValue(component),
          "Component instance " + component + " is already registered in feature " + id);

        component.init(this);
        components.put(component.getClass(), component);

        if (enabled) {
            component.enable();
        }
    }

    @Override
    public void removeComponent(@NotNull FeatureComponent component) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Components can only be removed from the main thread");

        FeatureComponent removed = components.remove(component.getClass());
        Validate.isTrue(removed != null,
          "Component instance " + component + " is not registered in feature " + id);


        if (enabled) {
            component.disable();
        }
    }

    @Override
    public boolean hasComponent(@NotNull FeatureComponent component) {
        return components.get(component.getClass()) == component;
    }

    @Override
    public boolean hasComponent(@NotNull Class<? extends FeatureComponent> component) {
        return components.containsKey(component);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FeatureComponent> T getComponent(@NotNull Class<T> componentClass) {
        return (T) components.get(componentClass);
    }

    @Override
    public @NotNull Collection<FeatureComponent> getComponents() {
        return Collections.unmodifiableCollection(components.values());
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
