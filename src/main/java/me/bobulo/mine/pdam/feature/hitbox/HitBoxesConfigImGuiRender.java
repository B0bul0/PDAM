package me.bobulo.mine.pdam.feature.hitbox;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiColorEditFlags;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.util.ColorUtil;
import net.minecraft.entity.EntityList;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.feature.hitbox.HitBoxes.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class HitBoxesConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    private float[] color;

    private List<String> entityNameList;
    private ImGuiTextFilter entityFilter = new ImGuiTextFilter();

    public HitBoxesConfigImGuiRender() {
        this.color = ColorUtil.toRgba(COLOR.get());
        entityNameList = new ArrayList<>(EntityList.getEntityNameList());
        entityNameList.add(0, "Player");
    }

    @Override
    public void draw() {
        if (colorEdit4("Color", color,
          ImGuiColorEditFlags.AlphaBar | ImGuiColorEditFlags.AlphaPreview
            | ImGuiColorEditFlags.NoInputs)) {
            COLOR.set(ColorUtil.toArgbInt(color));
        }

        sameLine();

        if (button("Reset Color")) {
            COLOR.set(DEFAULT_COLOR);
            color = ColorUtil.toRgba(DEFAULT_COLOR);
        }

        if (checkbox("Only Target", ONLY_TARGET.get())) {
            ONLY_TARGET.set(!ONLY_TARGET.get());
        }

        tooltip("Only render hitboxes for the current target");

        if (checkbox("Show Invisible Entities", SHOW_INVISIBLE.get())) {
            SHOW_INVISIBLE.set(!SHOW_INVISIBLE.get());
        }

        tooltip("Show hitboxes for invisible entities.");

        if (checkbox("Show Expanded", SHOW_EXPANDED.get())) {
            SHOW_EXPANDED.set(!SHOW_EXPANDED.get());
        }

        tooltip("Show the expanded hitbox (bounding box + attack bounding box) for living entities.");

        separatorText("Entity Types");

        if (checkbox("Enable Entity Types", ENABLE_ENTITY_TYPES.get())) {
            ENABLE_ENTITY_TYPES.set(!ENABLE_ENTITY_TYPES.get());
        }
        tooltip("Enable/Disable hitboxes for specific entity types.");

        if (!ENABLE_ENTITY_TYPES.get()) {
            beginDisabled();
        }

        if (beginChild("##EntityTypes", 0, 200, true)) {
            text("Search");
            sameLine();
            entityFilter.draw("##EntityFilter");

            if (button("Select All")) {
                for (String entityName : entityNameList) {
                    EntityConfig entityConfig = getByEntity(entityName);
                    entityConfig.enabled.set(true);
                }
            }
            sameLine();
            if (button("Deselect All")) {
                for (String entityName : entityNameList) {
                    EntityConfig entityConfig = getByEntity(entityName);
                    entityConfig.enabled.set(false);
                }
            }

            separator();

            for (String entityName : entityNameList) {
                if (!entityFilter.passFilter(entityName)) continue;

                EntityConfig entityConfig = getByEntity(entityName);
                if (checkbox("##Enabled" + entityName, entityConfig.enabled.get())) {
                    entityConfig.enabled.set(!entityConfig.enabled.get());
                }

                tooltip("Toggle hitbox rendering for " + entityName);

                sameLine();

                float[] colorEntity = ColorUtil.toRgba(entityConfig.color.get());
                if (colorEdit4("Color##Color" + entityName, colorEntity,
                  ImGuiColorEditFlags.AlphaBar | ImGuiColorEditFlags.AlphaPreview
                    | ImGuiColorEditFlags.NoInputs | ImGuiColorEditFlags.NoLabel)) {
                    entityConfig.color.set(ColorUtil.toArgbInt(colorEntity));
                }

                tooltip("Edit the hitbox color for " + entityName);

                sameLine();
                text(entityName);
            }
        }
        endChild();

        if (!ENABLE_ENTITY_TYPES.get()) {
            endDisabled();
        }

    }

}
