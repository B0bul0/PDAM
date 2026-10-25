package me.bobulo.mine.pdam.feature.bungeebypass;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.ConfigMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

/**
 * Owns the BungeeBypass feature and its handshake configuration.
 */
public final class BungeeBypassFeature extends AbstractFeature {

    public static final String FEATURE_ID = "bungee_bypass";

    public static final ConfigValue<String> SPOOFED_IP = ConfigProperty.of("bungee_bypass.spoofed_ip", "127.0.0.1");

    public static final ConfigValue<String> SPOOFED_UUID = ConfigProperty.of("bungee_bypass.spoofed_uuid", "00000000-0000-0000-0000-000000000000");

    public static final ConfigValue<String> INJECTED_HOST = ConfigProperty.of("bungee_bypass.injected_host", "localhost");

    public static boolean isFeatureEnabled() {
        return PDAM.getFeatureService().isFeatureEnabled(FEATURE_ID);
    }

    public BungeeBypassFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(false));
        context.getScope().registerBehavior(new BungeeBypassConfigImGuiRender());
        context.getScope().registerBehavior(new ConfigMenuImGuiRender(true));
    }
}
