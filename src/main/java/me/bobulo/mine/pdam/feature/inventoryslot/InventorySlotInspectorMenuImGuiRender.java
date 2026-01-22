package me.bobulo.mine.pdam.feature.inventoryslot;

import imgui.type.ImInt;
import me.bobulo.mine.pdam.feature.imgui.MenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.util.ColorUtil;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.feature.inventoryslot.InventorySlotInspector.*;

public class InventorySlotInspectorMenuImGuiRender extends AbstractFeatureModule implements MenuImGuiRender {

    private static final String[] OVERLAY_PRIORITY_OPTIONS = new String[]{
            "Background",
            "Foreground" // TOPMOST
    };

    private final ImInt overlayPriorityIndex = new ImInt(OVERLAY_PRIORITY.get());
    private final float[] color = ColorUtil.toRgb(COLOR.get());

    @Override
    public void draw() {
        if (beginMenu("Inventory Slot Inspector")) {

            if (menuItem("Enable Slot Overlay", "", ENABLED.get())) {
                ENABLED.set(!ENABLED.get());
            }

            if (colorEdit3("Text Color", color)) {
                COLOR.set(ColorUtil.toRgbInt(color));
            }

            sameLine();

            if (button("Reset Color")) {
                COLOR.set(0xFF373737);
            }

            if (combo("Overlay Priority", overlayPriorityIndex, OVERLAY_PRIORITY_OPTIONS)) {
                OVERLAY_PRIORITY.set(overlayPriorityIndex.get());
            }

            endMenu();
        }
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

}
