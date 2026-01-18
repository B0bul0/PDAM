package me.bobulo.mine.pdam.config;

import java.io.File;

/**
 * Service for managing the main configuration of the application.
 */
public final class ConfigService {

    // The main config instance
    private PersistentConfig mainConfig;

    public void init(File file) {
        this.mainConfig = new GsonFileConfig(file);
        this.mainConfig.loadConfig();
    }

    public Config getMainConfig() {
        return mainConfig;
    }

}
