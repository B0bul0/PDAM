package me.bobulo.mine.pdam.imgui.guizmo;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class RenderGuizmoHandler {

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        GuizmoImGui.capture();
    }

}