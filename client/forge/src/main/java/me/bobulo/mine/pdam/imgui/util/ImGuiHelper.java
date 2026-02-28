package me.bobulo.mine.pdam.imgui.util;

import imgui.ImGuiIO;

import static imgui.ImGui.*;

public final class ImGuiHelper {

    /**
     * Keeps the current window within the visible screen area.
     * <p>
     * Must be called within a {@code begin()}.
     */
    public static void keepInScreen() {
        final float marginVisible = 25.0f;

        ImGuiIO io = getIO();
        float displayW = io.getDisplaySizeX();
        float displayH = io.getDisplaySizeY();

        float x = getWindowPosX();
        float y = getWindowPosY();
        float w = getWindowWidth();

        boolean shouldUpdate = false;
        if (x + w < marginVisible) {
            x = marginVisible - w;
            shouldUpdate = true;
        }

        if (x > displayW - marginVisible) {
            x = displayW - marginVisible;
            shouldUpdate = true;
        }

        if (y < 0) {
            y = 0;
            shouldUpdate = true;
        }

        if (y > displayH - marginVisible) {
            y = displayH - marginVisible;
            shouldUpdate = true;
        }

        if (shouldUpdate) {
            setWindowPos(x, y);
        }
    }

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
