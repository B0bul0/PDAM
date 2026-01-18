package me.bobulo.mine.pdam.config.exception;

/**
 * Exception thrown when there is an error saving a configuration.
 */
public class ConfigSaveException extends ConfigException {

    public ConfigSaveException() {
    }

    public ConfigSaveException(String message) {
        super(message);
    }

    public ConfigSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigSaveException(Throwable cause) {
        super(cause);
    }

}
