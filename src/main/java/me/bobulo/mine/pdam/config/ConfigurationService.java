package me.bobulo.mine.pdam.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ConfigurationService {

    private Configuration mainConfig;

    private final List<ConfigBinder> configBinders = new ArrayList<>();

    public void init(File file) {
        this.mainConfig = new Configuration(file);
        this.mainConfig.load();
    }

    public void registerBinder(ConfigBinder initializer) {
        this.configBinders.add(initializer);
        initializer.setConfiguration(mainConfig);
    }

    public void unregisterBinder(ConfigBinder initializer) {
        this.configBinders.remove(initializer);
    }

    public void syncAll() {
        for (ConfigBinder initializer : this.configBinders) {
            initializer.sync();
        }

        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
    }

    public Configuration getMainConfig() {
        return mainConfig;
    }

    public List<ConfigBinder> getConfigBinders() {
        return configBinders;
    }
}
