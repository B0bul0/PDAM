package me.bobulo.mine.pdam.feature.chunk;

import lombok.Getter;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;

import java.awt.*;

/**
 * Feature for visualizing chunk boundaries and sections in the Minecraft world.
 */
@Getter
public final class ChunkViewer {

    public static final String FEATURE_ID = "chunk_viewer";

    // Line Width
    public static final ConfigValue<Float> LINE_WIDTH = ConfigProperty.of(FEATURE_ID + ".line_width", 1.0f);

    // Current Chunk
    public static final ConfigValue<Boolean> SHOW_CURRENT_CHUNK = ConfigProperty.of(FEATURE_ID + ".current_chunk.show", true);
    public static final ConfigValue<Integer> CURRENT_CHUNK_COLOR = ConfigProperty.of(FEATURE_ID + ".current_chunk.color", Color.BLUE.getRGB());

    // Highlighting
    public static final ConfigValue<Boolean> HIGHLIGHT_CURRENT_SECTION = ConfigProperty.of(FEATURE_ID + ".highlight.current_section", true);
    public static final ConfigValue<Integer> HIGHLIGHT_COLOR = ConfigProperty.of(FEATURE_ID + ".highlight.color", Color.BLUE.getRGB());

    // Surrounding chunks
    public static final ConfigValue<Boolean> SHOW_SURROUNDING = ConfigProperty.of(FEATURE_ID + ".surrounding.show", true);
    public static final ConfigValue<Integer> SURROUNDING_RADIUS = ConfigProperty.of(FEATURE_ID + ".surrounding.radius", 1);
    public static final ConfigValue<Integer> SURROUNDING_COLOR = ConfigProperty.of(FEATURE_ID + ".surrounding.color", Color.RED.getRGB());

    // Sections
    public static final ConfigValue<Boolean> SHOW_SECTIONS = ConfigProperty.of(FEATURE_ID + "section.show", true);
    public static final ConfigValue<Integer> VERTICAL_SECTIONS = ConfigProperty.of(FEATURE_ID + ".section.vertical_sections", 16);
    public static final ConfigValue<Integer> SECTION_COLOR = ConfigProperty.of(FEATURE_ID + ".section.color", Color.YELLOW.getRGB());

    private ChunkViewer() {
    }

}
