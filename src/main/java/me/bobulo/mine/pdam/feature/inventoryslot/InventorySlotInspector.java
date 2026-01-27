package me.bobulo.mine.pdam.feature.inventoryslot;

import lombok.Getter;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;

@Getter
public final class InventorySlotInspector {

    public static final String FEATURE_ID = "inventory_slot_inspector";

    public static final int DEFAULT_COLOR = 0x6E373737;

    public static final ConfigValue<Integer> COLOR = ConfigProperty.of(FEATURE_ID + ".color", DEFAULT_COLOR);
    public static final ConfigValue<Integer> OVERLAY_PRIORITY = ConfigProperty.of(FEATURE_ID + ".overlay_priority", 0);

    private InventorySlotInspector() {}

}
