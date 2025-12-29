package me.bobulo.mine.pdam.ui;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages and renders all display elements (DisplayElement).
 */
@SideOnly(Side.CLIENT)
public final class UIManager {

    private final List<DisplayElement> elements = new CopyOnWriteArrayList<>();

    public UIManager() {
    }

    public void addElement(DisplayElement element) {
        if (!elements.contains(element)) {
            elements.add(element);
        }
    }

    public void removeElement(DisplayElement element) {
        elements.remove(element);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        for (DisplayElement element : elements) {
            element.render(event);
        }
    }
}