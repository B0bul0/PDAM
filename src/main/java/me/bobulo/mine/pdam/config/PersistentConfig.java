package me.bobulo.mine.pdam.config;

public interface PersistentConfig extends Config {

    void loadConfig();

    void saveConfig();

}
