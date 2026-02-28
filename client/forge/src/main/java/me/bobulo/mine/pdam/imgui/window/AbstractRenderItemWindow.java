package me.bobulo.mine.pdam.imgui.window;

import imgui.type.ImBoolean;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;

public abstract class AbstractRenderItemWindow implements ImGuiRenderable, ToolbarItemWindow {

    protected String menuName;
    protected final ImBoolean isVisible = new ImBoolean(false);

    protected AbstractRenderItemWindow(String menuName) {
        this.menuName = menuName;
    }

    @Override
    public final void newFrame() {
        if (!isVisible.get()) {
            return;
        }

        renderGui();
    }

    public abstract void renderGui();

    @Override
    public String getMenuName() {
        return menuName;
    }

    @Override
    public boolean isVisible() {
        return isVisible.get();
    }

    @Override
    public void setVisible(boolean visible) {
        isVisible.set(visible);
    }

    @Override
    public void toggleVisible() {
        isVisible.set(!isVisible.get());
    }

}
