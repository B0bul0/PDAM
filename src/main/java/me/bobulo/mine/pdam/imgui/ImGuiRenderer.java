package me.bobulo.mine.pdam.imgui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.backend.ImGuiImplGl2;
import me.bobulo.mine.pdam.imgui.backend.ImGuiImplLwjgl2;
import me.bobulo.mine.pdam.imgui.guizmo.RenderGuizmoHandler;
import me.bobulo.mine.pdam.imgui.guizmo.GuizmoImGui;
import me.bobulo.mine.pdam.imgui.input.ImGuiInputHandler;
import me.bobulo.mine.pdam.imgui.toolbar.ImGuiToolbar;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SideOnly(Side.CLIENT)
public final class ImGuiRenderer {

    private static final Logger log = LogManager.getLogger(ImGuiRenderer.class);

    private boolean initialized;
    private ImGuiImplLwjgl2 imGuiImplDisplay;
    private ImGuiImplGl2 imGuiImplGl2;
    private ImGuiInputHandler inputHandler;

    // Flag to cancel rendering in case of error
    private boolean cancelRender;

    private ImGuiToolbar imGuiToolbar;

    private final List<ImGuiRenderable> frameRenders = new CopyOnWriteArrayList<>();

    public ImGuiRenderer() {
    }

    public void registerWindow(Object window) {
        if (window instanceof ImGuiRenderable) {
            addFrameRender((ImGuiRenderable) window);
        }
    }

    public void unregisterWindow(Object window) {
        if (window instanceof ImGuiRenderable) {
            removeFrameRender((ImGuiRenderable) window);
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
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setIniFilename(new File(PDAM.getConfigDirectory(), "pdam_layout.ini").getAbsolutePath());

        imGuiImplGl2 = new ImGuiImplGl2();
        imGuiImplGl2.init();

        imGuiImplDisplay = new ImGuiImplLwjgl2();
        imGuiImplDisplay.init();

        inputHandler = new ImGuiInputHandler(imGuiImplDisplay);
        MinecraftForge.EVENT_BUS.register(inputHandler);

        MinecraftForge.EVENT_BUS.register(new RenderGuizmoHandler());

        imGuiToolbar = new ImGuiToolbar();

        initialized = true;
    }

    public void renderFrame() {
        if (!initialized || cancelRender) {
            return;
        }

        imGuiImplDisplay.newFrame();
        imGuiImplGl2.newFrame();

        ImGui.newFrame();
        GuizmoImGui.renderFrame();

        imGuiToolbar.newFrame();

        for (ImGuiRenderable frameRender : frameRenders) {
            try {
                frameRender.newFrame();
            } catch (Exception exception) {
                cancelRender = true;
                log.error("Error while rendering ImGui frame:", exception);
            }
        }

        if (cancelRender) {
            ImGui.endFrame();
            return;
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
        imGuiImplDisplay.readKey();
    }

}