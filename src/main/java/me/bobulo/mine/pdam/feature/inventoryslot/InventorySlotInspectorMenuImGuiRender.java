package me.bobulo.mine.pdam.feature.inventoryslot;

import imgui.flag.ImGuiColorEditFlags;
import imgui.type.ImInt;
import me.bobulo.mine.pdam.feature.imgui.MenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.util.ColorUtil;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.feature.inventoryslot.InventorySlotInspector.DEFAULT_COLOR;

public class InventorySlotInspectorMenuImGuiRender extends AbstractFeatureModule implements MenuImGuiRender {

    private static final String[] OVERLAY_PRIORITY_OPTIONS = new String[]{
      "Background",
      "Foreground"
    };

    private final InventorySlotInspector context;

    private final ImInt overlayPriorityIndex;
    private float[] color;

    public InventorySlotInspectorMenuImGuiRender(InventorySlotInspector context) {
        this.context = context;
        this.overlayPriorityIndex = new ImInt(context.getOverlayPriorityConfig().get());
        this.color = ColorUtil.toRgba(context.getColorConfig().get());
    }

    @Override
    public void draw() {
        if (beginMenu("Inventory Slot Inspector")) {

            if (checkbox("Enable Slot Overlay", context.getFeature().isEnabled())) {
                context.getEnabledConfig().set(!context.getEnabledConfig().get());
            }

            separator();

            if (colorEdit4("Text Color", color,
              ImGuiColorEditFlags.AlphaBar | ImGuiColorEditFlags.AlphaPreview)) {
                context.getColorConfig().set(ColorUtil.toArgbInt(color));
            }

            sameLine();

            if (button("Reset Color")) {
                context.getColorConfig().set(DEFAULT_COLOR);
                color = ColorUtil.toRgba(DEFAULT_COLOR);
            }

            if (combo("Overlay Priority", overlayPriorityIndex, OVERLAY_PRIORITY_OPTIONS)) {
                context.getOverlayPriorityConfig().set(overlayPriorityIndex.get());
            }

            endMenu();
        }
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

}
