package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.config.exception.ConfigLoadException;
import me.bobulo.mine.pdam.config.exception.ConfigSaveException;

/**
 * A configuration that supports loading from and saving to a persistent storage.
 */
public interface PersistentConfig extends Config {

    /**
     * Loads the configuration from persistent storage.
     * @throws ConfigLoadException if an error occurs during loading
     */
    void loadConfig();

    /**
     * Saves the configuration to persistent storage.
     * @throws ConfigSaveException if an error occurs during saving
     */
    void saveConfig();

}
