package me.bobulo.mine.pdam.feature.chunk;

import imgui.flag.ImGuiColorEditFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.util.ColorUtil;
import me.bobulo.mine.pdam.util.MathUtil;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.feature.chunk.ChunkViewer.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.*;

/**
 * Renders the ImGui configuration menu for the ChunkViewer feature.
 */
public final class ChunkConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    private final float[] highlightColor = ColorUtil.toRgba(HIGHLIGHT_COLOR.get());
    private final float[] surroundingColor = ColorUtil.toRgba(SURROUNDING_COLOR.get());
    private final float[] sectionColor = ColorUtil.toRgba(SECTION_COLOR.get());
    private final float[] currentChunkColor = ColorUtil.toRgba(CURRENT_CHUNK_COLOR.get());

    private final ImInt radius = new ImInt(SURROUNDING_RADIUS.get());
    private final ImInt verticalSections = new ImInt(VERTICAL_SECTIONS.get());
    private final ImFloat lineWidth = new ImFloat(LINE_WIDTH.get());

    @Override
    public void draw() {

        if (inputFloat("Line Width", lineWidth)) {
            lineWidth.set(Math.max(0.0f, lineWidth.get()));
            LINE_WIDTH.set(lineWidth.get());
        }

        // Current Chunk
        spacing();
        separatorText("Current Chunk");

        if (checkbox("Show Current Chunk", SHOW_CURRENT_CHUNK.get())) {
            SHOW_CURRENT_CHUNK.set(!SHOW_CURRENT_CHUNK.get());
        }

        beginDisableIf(!SHOW_CURRENT_CHUNK.get());
        color("Current Chunk Color", currentChunkColor, CURRENT_CHUNK_COLOR);
        endDisableIf(!SHOW_CURRENT_CHUNK.get());

        // Highlight Current Section
        spacing();
        separatorText("Current Section Highlight");

        if (checkbox("Highlight Current Section", HIGHLIGHT_CURRENT_SECTION.get())) {
            HIGHLIGHT_CURRENT_SECTION.set(!HIGHLIGHT_CURRENT_SECTION.get());
        }

        tooltip("Highlight the current chunk section the player is in, even through walls.");

        color("Highlight Color", highlightColor, HIGHLIGHT_COLOR);

        // Surrounding Chunks
        spacing();
        separatorText("Surrounding Chunks");

        if (checkbox("Show Surrounding Chunks", SHOW_SURROUNDING.get())) {
            SHOW_SURROUNDING.set(!SHOW_SURROUNDING.get());
        }

        beginDisableIf(!SHOW_SURROUNDING.get());

        if (inputInt("Surrounding Radius", radius)) {
            radius.set(Math.min(0, radius.get()));
            SURROUNDING_RADIUS.set(radius.get());
        }

        color("Surrounding Color", surroundingColor, SURROUNDING_COLOR);

        endDisableIf(!SHOW_SURROUNDING.get());

        // Sections
        spacing();
        separatorText("Sections");

        if (checkbox("Show Sections", SHOW_SECTIONS.get())) {
            SHOW_SECTIONS.set(!SHOW_SECTIONS.get());
        }

        beginDisableIf(!SHOW_SECTIONS.get());

        if (inputInt("Vertical Sections", verticalSections)) {
            verticalSections.set(MathUtil.clamp(verticalSections.get(), 0, 16));
            VERTICAL_SECTIONS.set(verticalSections.get());
        }

        color("Section Color", sectionColor, SECTION_COLOR);

        endDisableIf(!SHOW_SECTIONS.get());
    }

    private void color(String label, float[] color, ConfigValue<Integer> colorConfig) {
        if (colorEdit4(label, color,
          ImGuiColorEditFlags.AlphaBar | ImGuiColorEditFlags.AlphaPreview
            | ImGuiColorEditFlags.NoInputs)) {
            colorConfig.set(ColorUtil.toArgbInt(color));
        }

        sameLine();

        if (button("Reset Color##ResetColor" + label)) {
            colorConfig.set(null);

            float[] rgba = ColorUtil.toRgba(colorConfig.get());
            color[0] = rgba[0];
            color[1] = rgba[1];
            color[2] = rgba[2];
            color[3] = rgba[3];
        }
    }

}
