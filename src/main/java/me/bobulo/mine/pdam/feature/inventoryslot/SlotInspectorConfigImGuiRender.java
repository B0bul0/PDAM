package me.bobulo.mine.pdam.feature.inventoryslot;

import imgui.flag.ImGuiColorEditFlags;
import imgui.type.ImInt;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.util.ColorUtil;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.feature.inventoryslot.InventorySlotInspector.*;

public final class SlotInspectorConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    private static final String[] OVERLAY_PRIORITY_OPTIONS = new String[]{
      "Background",
      "Foreground"
    };

    private final ImInt overlayPriorityIndex;
    private float[] color;

    public SlotInspectorConfigImGuiRender() {
        this.overlayPriorityIndex = new ImInt(OVERLAY_PRIORITY.get());
        this.color = ColorUtil.toRgba(COLOR.get());
    }

    @Override
    public void draw() {
        if (checkbox("Enable Slot Overlay", getFeature().isEnabled())) {
            getFeature().toggleEnabled();
        }

        separator();

        if (colorEdit4("Text Color", color,
          ImGuiColorEditFlags.AlphaBar | ImGuiColorEditFlags.AlphaPreview)) {
            COLOR.set(ColorUtil.toArgbInt(color));
        }

        sameLine();

        if (button("Reset Color")) {
            COLOR.set(DEFAULT_COLOR);
            color = ColorUtil.toRgba(DEFAULT_COLOR);
        }

        if (combo("Overlay Priority", overlayPriorityIndex, OVERLAY_PRIORITY_OPTIONS)) {
            OVERLAY_PRIORITY.set(overlayPriorityIndex.get());
        }
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

}
