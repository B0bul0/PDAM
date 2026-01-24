package me.bobulo.mine.pdam.imgui.window;

import imgui.type.ImBoolean;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;

public abstract class AbstractPopupRenderItemWindow implements ImGuiRenderable {

    protected final ImBoolean isVisible = new ImBoolean(false);

    @Override
    public final void newFrame() {
        if (!isVisible.get()) {
            return;
        }

        renderGui();

        if (!isVisible.get()) {
            PDAM.getImGuiRenderer().unregisterWindow(this);
        }
    }

    public void open() {
        if (isVisible.get()) {
            return;
        }

        isVisible.set(true);
        PDAM.getImGuiRenderer().registerWindow(this);
    }

    public void close() {
        isVisible.set(false);
    }

    public abstract void renderGui();

}
