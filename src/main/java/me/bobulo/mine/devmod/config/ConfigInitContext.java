package me.bobulo.mine.devmod.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigInitContext {

    private final String categoryName;
    private final List<PropertySpec<?>> properties = new ArrayList<>();

    public ConfigInitContext(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
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