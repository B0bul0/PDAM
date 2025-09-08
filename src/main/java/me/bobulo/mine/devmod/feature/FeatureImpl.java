package me.bobulo.mine.devmod.feature;

import me.bobulo.mine.devmod.feature.component.FeatureComponent;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureImpl implements Feature {

    private final String id;
    private final String name;
    private boolean enabled = false;

    private final List<Runnable> onEnable = new ArrayList<>();
    private final List<Runnable> onDisable = new ArrayList<>();

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
    public void enable() {
        if (!enabled) {
            onEnable();
            enabled = true;
        }
    }

    @Override
    public void disable() {
        if (enabled) {
            onDisable();
            enabled = false;
        }
    }

    private void onEnable() {
        for (Runnable r : onEnable) {
            r.run();
        }

        for (FeatureComponent component : components) {
            component.enable();
        }
    }

    private void onDisable() {
        for (Runnable r : onDisable) {
            r.run();
        }

        for (FeatureComponent component : components) {
            component.disable();
        }
    }

    /* Register listeners */

    public void registerOnEnable(Runnable r) {
        onEnable.add(r);
    }

    public void unregisterOnEnable(Runnable r) {
        onEnable.remove(r);
    }

    public void registerOnDisable(Runnable r) {
        onDisable.add(r);
    }

    public void unregisterOnDisable(Runnable r) {
        onDisable.remove(r);
    }

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
