package me.bobulo.mine.pdam.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Collections;
import java.util.List;

/**
 * A DisplayElement that renders a list of strings inside a semi-transparent box.
 */
public class InfoBox implements DisplayElement {

    private List<String> lines = Collections.emptyList();
    private final int x;
    private final int y;
    private final int padding;

    public InfoBox(int x, int y, int padding) {
        this.x = x;
        this.y = y;
        this.padding = padding;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public void render(RenderGameOverlayEvent.Post event) {
        if (lines.isEmpty()) {
            return;
        }

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(line));
        }

        int boxWidth = maxWidth + (padding * 2);
        int boxHeight = (lines.size() * (fontRenderer.FONT_HEIGHT + 1)) + (padding * 2) - 2;

        Gui.drawRect(x, y, x + boxWidth, y + boxHeight, 0x90000000);

        int lineY = y + padding;
        for (String line : lines) {
            fontRenderer.drawStringWithShadow(line, x + padding, lineY, 0xFFFFFF);
            lineY += fontRenderer.FONT_HEIGHT + 1;
        }
    }
}