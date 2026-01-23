package me.bobulo.mine.pdam.feature.inventoryslot;

import lombok.Getter;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.context.FeatureContext;

@Getter
public final class InventorySlotInspector extends FeatureContext {

    public static final String FEATURE_ID = "inventory_slot_inspector";

    private static InventorySlotInspector instance;

    public static InventorySlotInspector context() {
        if (instance == null) {
            instance = new InventorySlotInspector();
        }

        return instance;
    }

    public static final int DEFAULT_COLOR = 0x6E373737;

    private final ConfigProperty<Boolean> enabledConfig = createEnabledConfig(false).sync();
    private final ConfigValue<Integer> colorConfig = createConfigValue("color", DEFAULT_COLOR);
    private final ConfigValue<Integer> overlayPriorityConfig = createConfigValue("overlay_priority", 0);

    public InventorySlotInspector() {
        super(FEATURE_ID);

        addModules(
          new InventorySlotInspectorMenuImGuiRender(this)
        );

    }

    public ConfigValue<Boolean> getEnabledConfig() {
        return enabledConfig;
    }
}
