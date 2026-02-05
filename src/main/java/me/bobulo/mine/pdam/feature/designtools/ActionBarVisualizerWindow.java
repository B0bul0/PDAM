package me.bobulo.mine.pdam.feature.designtools;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;

public final class ActionBarVisualizerWindow extends AbstractRenderItemWindow {

    private final ImString text = new ImString(256);

    public ActionBarVisualizerWindow() {
        super("Action Bar Visualizer");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(244, 100, ImGuiCond.Once);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Action Bar Visualizer##ActionBarVisualizerWindow", isVisible, ImGuiWindowFlags.NoResize)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        text("Action Bar Text:");
        inputText("##BarText", text);
        spacing();

        if (button("Preview Action Bar")) {
            playPreview(text.get());
        }

    }

    private void playPreview(String text) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.ingameGUI == null) {
            return;
        }

        mc.ingameGUI.setRecordPlaying(text, false);
    }

}
