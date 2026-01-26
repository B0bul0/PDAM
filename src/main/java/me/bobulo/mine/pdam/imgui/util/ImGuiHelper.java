package me.bobulo.mine.pdam.imgui.util;

import static imgui.ImGui.*;

public final class ImGuiHelper {

    /**
     * Displays a tooltip with the given description when the item is hovered.
     */
    public static void tooltip(String description) {
        if (description == null || description.isEmpty()) {
            return;
        }

        if (isItemHovered()) {
            beginTooltip();
            textUnformatted(description);
            endTooltip();
        }
    }

    public static void helpMarker(String description) {
        textDisabled("(?)");
        tooltip(description);
    }

    public static void separatorWithSpacing() {
        spacing();
        separator();
        spacing();
    }

    private ImGuiHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
