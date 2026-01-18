package me.bobulo.mine.pdam.config;

import java.io.File;

public final class ConfigService {

    private PersistentConfig mainConfig;

    public void init(File file) {
        this.mainConfig = new GsonFileConfig(file);
        this.mainConfig.loadConfig();
    }

    public Config getMainConfig() {
        return mainConfig;
    }

}
