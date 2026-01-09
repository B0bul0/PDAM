package me.bobulo.mine.pdam.imgui.toolbar;

public interface ToolbarItemWindow {

    String getMenuName();

    boolean isVisible();

    void setVisible(boolean visible);

    void toggleVisible();

}
