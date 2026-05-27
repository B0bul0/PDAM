package me.bobulo.mine.pdam.imgui.toolbar;

/**
 * Represents a window that can be toggled from the toolbar menu in ImGui.
 */
public interface ToolbarItemWindow {

    String getMenuName();

    boolean isVisible();

    void setVisible(boolean visible);

    void toggleVisible();

}
