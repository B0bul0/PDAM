package me.bobulo.mine.devmod.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigBinder {

    private final String categoryName;
    private final List<PropertySpec<?>> properties = new ArrayList<>();

    private Configuration configuration;

    public ConfigBinder(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setConfiguration(Configuration configuration) {
        if (this.configuration != null) {
            throw new IllegalStateException("Configuration is already set");
        }

        this.configuration = configuration;
    }

    public void initialize(PropertyDeclarer propertyDeclarer) {
        ConfigInitContext configInitContext = new ConfigInitContext(this);
        PropertyFactory propertyFactory = new PropertyFactory(configuration);

        propertyDeclarer.initProperties(configInitContext);

        // Create, load and register properties
        List<PropertySpec<?>> properties = configInitContext.getProperties();
        for (PropertySpec<?> property : properties) {
            propertyFactory.create(categoryName, property);
            this.properties.add(property);
        }

        sync();
    }

    @SuppressWarnings("unchecked")
    public void sync() {
        ConfigCategory configCategory = configuration.getCategory(categoryName);
        for (PropertySpec<?> spec : properties) {
            Consumer<?> onUpdateCallback = spec.getOnUpdateCallback();
            Property property = configCategory.get(spec.getName());
            if (property == null || onUpdateCallback == null) {
                continue;
            }

            PropertySpec.Type type = spec.getType();
            switch (type) {
                case STRING:
                    ((Consumer<String>) onUpdateCallback).accept(property.getString());
                    break;
                case INTEGER:
                    ((Consumer<Integer>) onUpdateCallback).accept(property.getInt());
                    break;
                case BOOLEAN:
                    ((Consumer<Boolean>) onUpdateCallback).accept(property.getBoolean());
                    break;
                case DOUBLE:
                    ((Consumer<Double>) onUpdateCallback).accept(property.getDouble());
                    break;
                case STRING_ARRAY:
                    ((Consumer<String[]>) onUpdateCallback).accept(property.getStringList());
                    break;
                case INTEGER_ARRAY:
                    ((Consumer<int[]>) onUpdateCallback).accept(property.getIntList());
                    break;
                case BOOLEAN_ARRAY:
                    ((Consumer<boolean[]>) onUpdateCallback).accept(property.getBooleanList());
                    break;
                case DOUBLE_ARRAY:
                    ((Consumer<double[]>) onUpdateCallback).accept(property.getDoubleList());
                    break;
                default:
                    throw new IllegalStateException("Unsupported property type: " + type);
            }
        }
    }

}