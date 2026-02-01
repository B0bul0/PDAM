package me.bobulo.mine.pdam.feature.bungeebypass;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;

/**
 * BungeeBypass feature configuration and utility class.
 */
public final class BungeeBypass {

    public static final String FEATURE_ID = "bungee_bypass";

    public static final ConfigValue<String> SPOOFED_IP = ConfigProperty.of("bungee_bypass.spoofed_ip", "127.0.0.1");

    public static final ConfigValue<String> SPOOFED_UUID = ConfigProperty.of("bungee_bypass.spoofed_uuid", "00000000-0000-0000-0000-000000000000");

    public static final ConfigValue<String> INJECTED_HOST = ConfigProperty.of("bungee_bypass.injected_host", "localhost");

    public static boolean isFeatureEnabled() {
        return PDAM.getFeatureService().isFeatureEnabled(FEATURE_ID);
    }

    private BungeeBypass() {
    }

}
