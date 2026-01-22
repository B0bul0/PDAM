package me.bobulo.mine.pdam.feature.inventoryslot;

import me.bobulo.mine.pdam.config.ConfigElement;
import me.bobulo.mine.pdam.config.ConfigValue;

public final class InventorySlotInspector  {

    public static final ConfigValue<Boolean> ENABLED = ConfigElement.of(
      "inventoryslotinspector.enabled",
      true
    );

    public static final ConfigValue<Integer> COLOR = ConfigElement.of(
      "inventoryslotinspector.color",
      0xFF373737
    );

    public static final ConfigValue<Integer> OVERLAY_PRIORITY = ConfigElement.of(
      "inventoryslotinspector.OverlayPriority",
      0
    );

}
