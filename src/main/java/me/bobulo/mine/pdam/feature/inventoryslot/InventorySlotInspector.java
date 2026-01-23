package me.bobulo.mine.pdam.feature.inventoryslot;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.context.FeatureContext;
import me.bobulo.mine.pdam.feature.module.ForgerListenerFeatureModule;

public final class InventorySlotInspector extends FeatureContext {

    public static final ConfigValue<Boolean> ENABLED = ConfigProperty.of(
        "inventory_slot_inspector.enabled",
        true
      ).onChange(enabled -> {
          if (enabled) {
              get().getFeature().enable();
          } else {
              get().getFeature().disable();
          }
      }).sync();

    public static final int DEFAULT_COLOR = 0xFF373737;

    public static final ConfigValue<Integer> COLOR = ConfigProperty.of(
      "inventory_slot_inspector.color",
      DEFAULT_COLOR
    );

    public static final ConfigValue<Integer> OVERLAY_PRIORITY = ConfigProperty.of(
      "inventory_slot_inspector.overlay_priority",
      0
    );

    // Singleton instance

    private static InventorySlotInspector instance;

    public static InventorySlotInspector get() {
        if (instance == null) {
            instance = new InventorySlotInspector();
        }
        return instance;
    }

    public InventorySlotInspector() {
        super("inventory_slot_inspector");
    }

    @Override
    protected void setup() {
        addModules(
          ForgerListenerFeatureModule.of(new InventorySlotListener()),
          new InventorySlotInspectorMenuImGuiRender()
        );
    }
}
