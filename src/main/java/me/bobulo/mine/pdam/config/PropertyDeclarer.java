package me.bobulo.mine.pdam.config;

/**
 * Represents an object that can declare its own configuration properties.
 */
public interface PropertyDeclarer {

    /**
     * Initializes the properties for this object.
     * <p>
     * Implementations should use the provided {@link ConfigInitContext} to create and register
     * their {@link PropertySpec}s. This method is called by the configuration system
     * when it's time to build the configuration for this object.
     *
     * @param context The context for property initialization, providing methods to create properties. This is never {@code null}.
     */
    default void initProperties(ConfigInitContext context) {

    }

}