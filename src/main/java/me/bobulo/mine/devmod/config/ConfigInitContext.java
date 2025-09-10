package me.bobulo.mine.devmod.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigInitContext {

    private final ConfigBinder configBinder;
    private final List<PropertySpec<?>> properties = new ArrayList<>();

    public ConfigInitContext(ConfigBinder configBinder) {
        this.configBinder = configBinder;
    }

    public ConfigBinder getConfigBinder() {
        return configBinder;
    }

    public String getCategoryName() {
        return configBinder.getCategoryName();
    }

    public void addProperty(PropertySpec<?> property) {
        properties.add(property);
    }

    public <T> PropertySpec<T> createProperty(String name, T defaultValue) {
        PropertySpec<T> builder = PropertySpec.create(name, defaultValue);
        properties.add(builder);
        return builder;
    }

    public List<PropertySpec<?>> getProperties() {
        return properties;
    }
}