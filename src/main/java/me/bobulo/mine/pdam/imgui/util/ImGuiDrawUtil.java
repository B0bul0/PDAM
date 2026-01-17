package me.bobulo.mine.pdam.imgui.util;

import imgui.ImGui;
import imgui.ImGuiIO;

public final class ImGuiDrawUtil {

    private ImGuiDrawUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void keepInScreen() {
        final float marginVisible = 25.0f;

        ImGuiIO io = ImGui.getIO();
        float displayW = io.getDisplaySizeX();
        float displayH = io.getDisplaySizeY();

        float x = ImGui.getWindowPosX();
        float y = ImGui.getWindowPosY();
        float w = ImGui.getWindowWidth();

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
            ImGui.setWindowPos(x, y);
        }
    }

}
