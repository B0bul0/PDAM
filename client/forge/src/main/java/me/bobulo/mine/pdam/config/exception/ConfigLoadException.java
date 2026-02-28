package me.bobulo.mine.pdam.config.exception;

/**
 * Exception thrown when there is an error loading a configuration.
 */
public class ConfigLoadException extends ConfigException {

    public ConfigLoadException() {
    }

    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigLoadException(Throwable cause) {
        super(cause);
    }

}
