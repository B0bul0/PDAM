package me.bobulo.mine.pdam.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the context for initializing configuration properties.
 * <p>
 * This class is passed to {@link PropertyDeclarer#initProperties(ConfigInitContext)} to allow
 * features and components to declare their required properties. It acts as a temporary
 * container for {@link PropertySpec}s before they are formally registered with the
 * configuration system. It also provides convenience methods for creating properties.
 */
public final class ConfigInitContext {

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