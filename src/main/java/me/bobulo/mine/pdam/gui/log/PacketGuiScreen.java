package me.bobulo.mine.pdam.gui.log;

import com.google.common.collect.Lists;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PacketGuiScreen extends GuiScreen {

    private List<DisplayPacketLogEntry> allLogs;
    private List<DisplayPacketLogEntry> filteredLogs = Lists.newArrayList();

    private GuiTextField searchField;

    private int scrollPos = 0;
    private int maxScroll = 0;
    private int selectedLine = -1;

    private final Object logsLock = new Object();

    public PacketGuiScreen(List<DisplayPacketLogEntry> allLogs) {
        this.allLogs = allLogs;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        this.searchField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 150, 40, 150, 20);
        this.searchField.setMaxStringLength(50);
        this.searchField.setFocused(true);

        this.buttonList.add(new GuiButton(0, this.width - 80, this.height - 30, 60, 20, "Close"));
        this.buttonList.add(new GuiButton(1, this.width - 150, this.height - 30, 60, 20, "Clear"));

        filterLogs();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(null);
        } else if (button.id == 1) {
            allLogs.clear();
            filterLogs();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            filterLogs();
        } else if (keyCode == Keyboard.KEY_C && isCtrlKeyDown()) {
            if (selectedLine != -1 && selectedLine < filteredLogs.size()) {
                setClipboardString(filteredLogs.get(selectedLine).getPacketData());
            }
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);

        int top = 90;
        int bottom = this.height - 40;
        int left = 20;
        int right = this.width - 20;

        if (mouseX > left && mouseX < right && mouseY > top && mouseY < bottom) {
            int yPos = top;
            for (int i = scrollPos; i < filteredLogs.size() && yPos < bottom; i++) {
                DisplayPacketLogEntry entry = filteredLogs.get(i);
                int slotHeight = calculateEntryHeight(entry, right - left);

                if (mouseY >= yPos && mouseY < yPos + slotHeight) {
                    entry.setExpanded(!entry.isExpanded());
                    selectedLine = i;
                    updateMaxScroll();
                    break;
                }

                yPos += slotHeight;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            if (dWheel > 0) {
                scrollPos = Math.max(0, scrollPos - 1);
            } else {
                scrollPos = Math.min(maxScroll, scrollPos + 1);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Title
        drawCenteredString(this.fontRendererObj, "Packet Log Viewer", this.width / 2, 20, 0xFFFFFF);

        // Search Field
        drawString(this.fontRendererObj, "Search:", this.width / 2 - 200, 45, 0xA0A0A0);
        this.searchField.drawTextBox();

        // Headers
        int headerY = 70;
        int left = 20;
        drawString(this.fontRendererObj, "Time", left + 5, headerY, 0xFFFF00);
        drawString(this.fontRendererObj, "Packet Name", left + 80, headerY, 0xFFFF00);
        drawString(this.fontRendererObj, "Packet Data", left + 200, headerY, 0xFFFF00);

        int top = 90;
        int bottom = this.height - 40;
        int right = this.width - 20;
        int yPos = top;

        // Entries
        for (int i = scrollPos; i < filteredLogs.size() && yPos < bottom; i++) {
            DisplayPacketLogEntry entry = filteredLogs.get(i);
            int slotHeight = calculateEntryHeight(entry, right - left);

            if (yPos + slotHeight > bottom) break;

            int bgColor = (i == selectedLine) ? 0x40FFFFFF : (i % 2 == 0 ? 0x20000000 : 0x10000000);
            drawRect(left, yPos, right - 10, yPos + slotHeight, bgColor);

            int textColor = (i == selectedLine) ? 0xFFFFE0 : 0xFFFFFF;

            // Time
            drawString(this.fontRendererObj, entry.getFormattedTime(), left + 5, yPos + 2, textColor);

            // Packet Name
            drawString(this.fontRendererObj, entry.getPacketName(), left + 80, yPos + 2, textColor);

            // Packet Data Info
            if (entry.isExpanded()) {
                List<String> lines = fontRendererObj.listFormattedStringToWidth(entry.getPacketData(), 280);
                int lineY = yPos + 2;
                for (String line : lines) {
                    drawString(this.fontRendererObj, line, left + 200, lineY, textColor);
                    lineY += fontRendererObj.FONT_HEIGHT;
                }
            } else {
                String abbreviated = entry.getPacketDataShort();
                drawString(this.fontRendererObj, abbreviated, left + 200, yPos + 2, textColor);
            }

            yPos += slotHeight;
        }

        // Scrollbar
        drawScrollbar(top, bottom, right);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawScrollbar(int top, int bottom, int right) {
        int scrollbarLeft = right - 6;
        int scrollbarHeight = bottom - top;
        drawRect(scrollbarLeft, top, right, bottom, 0x80000000);

        if (maxScroll > 0) {
            int totalContentHeight = getTotalContentHeight();
            int visibleContentHeight = bottom - top;
            int handleHeight = Math.max(10, (int) ((float) visibleContentHeight / totalContentHeight * scrollbarHeight));
            int handleTop = top + (int) ((float) getScrollPosInPixels() / (totalContentHeight - visibleContentHeight) * (scrollbarHeight - handleHeight));
            drawRect(scrollbarLeft, handleTop, right, handleTop + handleHeight, 0xFFC0C0C0);
        }
    }

    private int calculateEntryHeight(DisplayPacketLogEntry entry, int availableWidth) {
        int baseHeight = fontRendererObj.FONT_HEIGHT + 4;
        if (entry.isExpanded()) {
            List<String> lines = fontRendererObj.listFormattedStringToWidth(entry.getPacketData(), 280);
            return lines.size() * fontRendererObj.FONT_HEIGHT + 4;
        }
        return baseHeight;
    }

    private int getTotalContentHeight() {
        int height = 0;
        for (DisplayPacketLogEntry entry : filteredLogs) {
            height += calculateEntryHeight(entry, this.width - 40);
        }
        return Math.max(height, 1);
    }

    private int getScrollPosInPixels() {
        int pixels = 0;
        for (int i = 0; i < scrollPos && i < filteredLogs.size(); i++) {
            pixels += calculateEntryHeight(filteredLogs.get(i), this.width - 40);
        }
        return pixels;
    }

    public void refreshLogs(List<DisplayPacketLogEntry> newLogs) {
        synchronized (logsLock) {
            this.allLogs = newLogs;
        }
        filterLogs();
    }

    public void refreshLogs() {
        filterLogs();
    }

    private void filterLogs() {
        String searchText = searchField.getText().toLowerCase();

        synchronized (logsLock) {
            filteredLogs = allLogs.stream()
              .filter(log -> {
                  boolean matchesSearch = searchText.isEmpty() ||
                    log.getPacketName().toLowerCase().contains(searchText) ||
                    log.getPacketData().toLowerCase().contains(searchText);

                  return matchesSearch;
              })
              .collect(Collectors.toList());
        }

        updateMaxScroll();
        scrollPos = Math.min(scrollPos, maxScroll);
    }

    private void updateMaxScroll() {
        int visibleHeight = this.height - 130;
        maxScroll = Math.max(0, filteredLogs.size() - Math.max(1, visibleHeight / (fontRendererObj.FONT_HEIGHT + 4)));
    }

}
