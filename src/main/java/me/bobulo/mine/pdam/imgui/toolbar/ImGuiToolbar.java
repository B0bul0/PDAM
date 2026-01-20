package me.bobulo.mine.pdam.imgui.toolbar;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.feature.FeatureService;
import me.bobulo.mine.pdam.feature.imgui.MenuImGuiRender;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;

import java.util.List;

import static imgui.ImGui.*;

public final class ImGuiToolbar implements ImGuiRenderable {

    private final FeatureService featureService;

    public ImGuiToolbar() {
        this.featureService = PDAM.getFeatureService();
    }

    @Override
    public void newFrame() {
        int windowFlags = ImGuiWindowFlags.NoTitleBar
          | ImGuiWindowFlags.NoResize
          | ImGuiWindowFlags.NoMove
          | ImGuiWindowFlags.NoCollapse
          | ImGuiWindowFlags.NoScrollbar
          | ImGuiWindowFlags.NoScrollWithMouse
          | ImGuiWindowFlags.NoBringToFrontOnFocus
          | ImGuiWindowFlags.NoBackground
          | ImGuiWindowFlags.MenuBar;

        setNextWindowPos(0, 0, ImGuiCond.Always);
        setNextWindowSize(45, 40, ImGuiCond.Always);

        if (begin("##Toolbar", windowFlags)) {
            if (beginMenuBar()) {

                if (beginMenu("PDAM")) {

                    List<MenuImGuiRender> allComponents = featureService.getAllBehaviors(MenuImGuiRender.class);
                    for (MenuImGuiRender component : allComponents) {
                        component.draw();
                    }

                    endMenu();
                }

                endMenuBar();
            }
            end();
        }
    }

}
