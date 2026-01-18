package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.PDAM;

import java.io.File;

public final class ConfigFactory {

    public static Config createEmptyConfig() {
        return new InMemoryConfig();
    }

    public static Config createFileConfig(File file) {
        return new GsonFileConfig(file);
    }

    public static Config createFileConfig(String file) {
        return new GsonFileConfig(new File(PDAM.getConfigDirectory(), file));
    }

    private ConfigFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
