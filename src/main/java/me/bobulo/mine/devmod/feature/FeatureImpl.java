package me.bobulo.mine.devmod.feature;

import me.bobulo.mine.devmod.feature.component.FeatureComponent;

import java.util.ArrayList;
import java.util.List;

public class FeatureImpl implements Feature {

    private boolean enabled = false;
    private String name;

    private final List<Runnable> onEnable = new ArrayList<>();
    private final List<Runnable> onDisable = new ArrayList<>();

    private final List<FeatureComponent> components = new ArrayList<>();

    public FeatureImpl(String name) {
        this.name = name;
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
            component.onEnable();
        }
    }

    private void onDisable() {
        for (Runnable r : onDisable) {
            r.run();
        }

        for (FeatureComponent component : components) {
            component.onDisable();
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
        components.add(component);
    }

    public void removeComponent(FeatureComponent component) {
        components.remove(component);
    }

}
