package me.bobulo.mine.pdam.ui;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Represents an element that can be drawn on the game overlay.
 */
public interface DisplayElement {

    /**
     * Called to render the element on the screen.
     * @param event The overlay rendering event.
     */
    void render(RenderGameOverlayEvent.Post event);

}