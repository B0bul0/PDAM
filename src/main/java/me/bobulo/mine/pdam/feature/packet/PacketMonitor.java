package me.bobulo.mine.pdam.feature.packet;

import me.bobulo.mine.pdam.feature.context.FeatureContext;
import me.bobulo.mine.pdam.feature.module.EnabledFeatureModule;

public final class PacketMonitor extends FeatureContext {

    public static final String FEATURE_ID = "packet_monitor";

    // Singleton instance

    private static PacketMonitor instance;

    public static PacketMonitor context() {
        if (instance == null) {
            instance = new PacketMonitor();
        }
        return instance;
    }

    private PacketMonitor() {
        super(FEATURE_ID);

        addModules(
          new EnabledFeatureModule(true),
          new PacketMonitorFeatureModule(this)
        );
    }

}
