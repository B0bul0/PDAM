package me.bobulo.mine.pdam.ui;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages and renders all display elements (DisplayElement).
 */
@SideOnly(Side.CLIENT)
public final class UIManager {

    private static final Logger log = LogManager.getLogger(UIManager.class);

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
            try {
                element.render(event);
            } catch (Exception exception) {
                log.error("Error while rendering display element: {}",
                  element.getClass().getName(), exception);
            }
        }
    }
}