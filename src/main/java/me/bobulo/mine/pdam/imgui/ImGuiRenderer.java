package me.bobulo.mine.pdam.imgui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.backend.ImGuiImplGl2;
import me.bobulo.mine.pdam.imgui.backend.ImGuiImplLwjgl2;
import me.bobulo.mine.pdam.imgui.input.ImGuiInputHandler;
import me.bobulo.mine.pdam.imgui.toolbar.ImGuiToolbar;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class ImGuiRenderer {

    private static final Logger log = LogManager.getLogger(ImGuiRenderer.class);

    private boolean initialized;
    private ImGuiImplLwjgl2 imGuiImplDisplay;
    private ImGuiImplGl2 imGuiImplGl2;
    private ImGuiInputHandler inputHandler;

    private final ImGuiToolbar imGuiToolbar = new ImGuiToolbar();

    private final List<ImGuiRenderable> frameRenders = new ArrayList<>();

    public ImGuiRenderer() {
    }

    public void registerWidow(Object widow) {
        if (widow instanceof ImGuiRenderable) {
            addFrameRender((ImGuiRenderable) widow);
        }

        if (widow instanceof ToolbarItemWindow) {
            imGuiToolbar.registerWindow((ToolbarItemWindow) widow);
        }
    }

    public void unregisterWidow(Object widow) {
        if (widow instanceof ImGuiRenderable) {
            removeFrameRender((ImGuiRenderable) widow);
        }

        if (widow instanceof ToolbarItemWindow) {
            imGuiToolbar.unregisterWindow((ToolbarItemWindow) widow);
        }
    }

    /* Register frame renders */

    public void addFrameRender(ImGuiRenderable frameRender) {
        if (!frameRenders.contains(frameRender)) {
            frameRenders.add(frameRender);
        }
    }

    public void removeFrameRender(ImGuiRenderable frameRender) {
        frameRenders.remove(frameRender);
    }

    /* Getters */

    public ImGuiToolbar getImGuiToolbar() {
        return imGuiToolbar;
    }

    /* Lifecycle */

    public void init() {
        if (initialized) {
            return;
        }

        ImGui.createContext();

        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setIniFilename(new File(PDAM.getConfigDirectory(), "pdam_layout.ini").getAbsolutePath());

        imGuiImplGl2 = new ImGuiImplGl2();
        imGuiImplGl2.init();

        imGuiImplDisplay = new ImGuiImplLwjgl2();
        imGuiImplDisplay.init();

        inputHandler = new ImGuiInputHandler(imGuiImplDisplay);
        MinecraftForge.EVENT_BUS.register(inputHandler);

        initialized = true;
    }

    public void renderFrame() {
        if (!initialized) {
            return;
        }

        imGuiImplDisplay.newFrame();
        imGuiImplGl2.newFrame();

        ImGui.newFrame();

        imGuiToolbar.newFrame();

        for (ImGuiRenderable frameRender : frameRenders) {
            try {
                frameRender.newFrame();
            } catch (Exception exception) {
                try {
                    ImGui.end();
                } catch (Exception ignored) {
                    break;
                }

                log.error("Error while rendering ImGui frame:", exception);
            }
        }

        ImGui.render();
        imGuiImplGl2.renderDrawData(ImGui.getDrawData());
    }

    public void shutdown() {
        if (!initialized) {
            return;
        }

        initialized = false;
        ImGui.destroyContext();
    }

    public void onTick() {
        imGuiImplDisplay.onKey();
    }

}