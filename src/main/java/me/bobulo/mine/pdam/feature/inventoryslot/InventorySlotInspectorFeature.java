package me.bobulo.mine.pdam.feature.inventoryslot;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.ConfigMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

public final class InventorySlotInspectorFeature extends AbstractFeature {

    public static final String FEATURE_ID = "inventory_slot_inspector";

    public static final int DEFAULT_COLOR = 0x6E373737;

    public static final ConfigValue<Integer> COLOR = ConfigProperty.of(FEATURE_ID + ".color", DEFAULT_COLOR);
    public static final ConfigValue<Integer> OVERLAY_PRIORITY = ConfigProperty.of(FEATURE_ID + ".overlay_priority", 0);

    public InventorySlotInspectorFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        context.getScope().registerBehavior(new SlotInspectorConfigImGuiRender());
        context.getScope().registerBehavior(new ConfigMenuImGuiRender(true));
    }
}
