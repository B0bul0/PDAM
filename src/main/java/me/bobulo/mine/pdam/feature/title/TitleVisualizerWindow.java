package me.bobulo.mine.pdam.feature.title;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public final class TitleVisualizerWindow extends AbstractRenderItemWindow {

    private final ImString titleText = new ImString(256);
    private final ImString subtitleText = new ImString(256);

    private final ImInt fadeIn = new ImInt(10);
    private final ImInt stay = new ImInt(40);
    private final ImInt fadeOut = new ImInt(10);

    public TitleVisualizerWindow() {
        super("Title Visualizer");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(404,225, ImGuiCond.Once);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Title Visualizer##TitleVisualizerWindow", isVisible, ImGuiWindowFlags.NoResize)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        separatorText("Title Settings");
        inputText("Title Text", titleText);
        inputText("Subtitle Text", subtitleText);

        separatorText("Timing Settings (in ticks)");
        inputInt("Fade In (ticks)", fadeIn, 1, 20);
        inputInt("Stay (ticks)", stay, 1, 20);
        inputInt("Fade Out (ticks)", fadeOut, 1, 20);

        spacing();

        if (button("Preview Title")) {
            playTitlePreview();
        }

        sameLine();

        if (button("Clear Current Title")) {
            resetTitlePreview();
        }
    }

    private void playTitlePreview() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.ingameGUI == null) {
            return;
        }

        if (!titleText.get().isEmpty()) {
            mc.ingameGUI.displayTitle(
              titleText.get(),
              null,
              fadeIn.get(),
              stay.get(),
              fadeOut.get()
            );
        }

        if (!subtitleText.get().isEmpty()) {
            mc.ingameGUI.displayTitle(
              null,
              subtitleText.get(),
              fadeIn.get(),
              stay.get(),
              fadeOut.get()
            );
        }

        if (!subtitleText.get().isEmpty()) {
            mc.ingameGUI.displayTitle(
              null,
              null,
              fadeIn.get(),
              stay.get(),
              fadeOut.get()
            );
        }
    }

    private void resetTitlePreview() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.ingameGUI == null) {
            return;
        }

        mc.ingameGUI.displayTitle(null, null, -1, -1, -1);
        mc.ingameGUI.setDefaultTitlesTimes();
    }

}
