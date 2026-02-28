package me.bobulo.mine.pdam.feature.itemname;

import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.feature.itemname.ItemNameDebug.MAPPER_CONFIG;
import static me.bobulo.mine.pdam.feature.itemname.ItemNameDebug.OVERRIDE_ITEM_NAME;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class ItemNameMapperConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    private static final String[] mapperConfigOptions = new String[]{
      "VANILLA",
      "ITEM_ID",
      "LOCALIZED_NAME",
      "BUKKIT_1_8"
    };

    @Override
    public void draw() {
        if (beginCombo("Item Name Mapper", MAPPER_CONFIG.get())) {
            for (String mapperConfigOption : mapperConfigOptions) {
                if (selectable(mapperConfigOption, MAPPER_CONFIG.get().equals(mapperConfigOption))) {
                    MAPPER_CONFIG.set(mapperConfigOption);
                }
            }

            endCombo();
        }

        if (checkbox("Override Item Name", OVERRIDE_ITEM_NAME.get())) {
            OVERRIDE_ITEM_NAME.set(!OVERRIDE_ITEM_NAME.get());
        }

        tooltip("Override item names in tooltips with mapped names.");
    }

}
