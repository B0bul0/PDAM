package me.bobulo.mine.pdam.feature.packet.gui;

import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.notification.Notifier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.bobulo.mine.pdam.util.LocaleUtils.translateToLocal;

public class PacketLogGuiScreen extends GuiScreen {

    private List<DisplayPacketLogEntry> allLogs;
    private List<DisplayPacketLogEntry> filteredLogs = Collections.emptyList();

    private GuiTextField searchField;

    private int scrollPos = 0;
    private int maxScroll = 0;
    private int selectedLine = -1;

    private boolean isDraggingScrollbar = false;
    private int dragStartY = 0;
    private int dragStartScrollPos = 0;

    public PacketLogGuiScreen(List<DisplayPacketLogEntry> allLogs) {
        this.allLogs = allLogs;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        this.searchField = new GuiTextField(0, this.fontRendererObj, 72, 40, this.width / 2 - 82, 20);
        this.searchField.setMaxStringLength(100);
        this.searchField.setFocused(true);

        this.buttonList.add(new GuiButton(0, this.width - 80, this.height - 30, 60, 20, translate("close")));
        this.buttonList.add(new GuiButton(1, this.width - 150, this.height - 30, 60, 20, translate("clear")));

        String pauseResumeText = PacketMonitorFeatureComponent.INSTANCE.isLoggingPaused() ? translate("resume") : translate("pause");
        this.buttonList.add(new GuiButton(2, this.width - 220, this.height - 30, 60, 20, pauseResumeText));

        this.buttonList.add(new GuiButton(3, this.width - 290, this.height - 30, 60, 20, translate("copy")));

        filterLogs();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) { // close
            this.mc.displayGuiScreen(null);
        } else if (button.id == 1) { // clear
            allLogs.clear();
            filterLogs();
        } else if (button.id == 2) { // pause/resume
            PacketMonitorFeatureComponent.INSTANCE.pauseLogging(!PacketMonitorFeatureComponent.INSTANCE.isLoggingPaused());
            button.displayString = PacketMonitorFeatureComponent.INSTANCE.isLoggingPaused() ? translate("resume") : translate("pause");
        } else if (button.id == 3) { // pause/resume
            if (selectedLine != -1 && selectedLine < filteredLogs.size()) {
                setClipboardString(filteredLogs.get(selectedLine).getPacketData());
                Notifier.showSuccess(translateToLocal("pdam.general.copied_to_clipboard"));
            }
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
        int scrollbarLeft = right - 6;

        // Scrollbar drag start
        if (mouseX >= scrollbarLeft && mouseX <= right && mouseY >= top && mouseY <= bottom) {
            isDraggingScrollbar = true;
            dragStartY = mouseY;
            dragStartScrollPos = scrollPos;
            return;
        }

        // Line click
        if (mouseX > left && mouseX < right - 10 && mouseY > top && mouseY < bottom) {
            int yPos = top - scrollPos;
            for (int i = 0; i < filteredLogs.size(); i++) {
                DisplayPacketLogEntry entry = filteredLogs.get(i);
                int slotHeight = calculateEntryHeight(entry);

                if (mouseY >= Math.max(yPos, top) && mouseY < Math.min(yPos + slotHeight, bottom) && yPos + slotHeight > top) {
                    entry.setExpanded(!entry.isExpanded());
                    selectedLine = i;
                    updateMaxScroll();
                    break;
                }

                yPos += slotHeight;
                if (yPos > bottom) break;
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        // Scrollbar dragging
        if (isDraggingScrollbar && maxScroll > 0) {
            int top = 90;
            int bottom = this.height - 40;
            int scrollbarHeight = bottom - top;

            int totalContentHeight = getTotalContentHeight();
            int handleHeight = Math.max(10, (int) ((float) scrollbarHeight / totalContentHeight * scrollbarHeight));

            int deltaY = mouseY - dragStartY;
            float scrollRatio = (float) deltaY / (scrollbarHeight - handleHeight);
            int scrollDelta = (int) (scrollRatio * maxScroll);

            scrollPos = Math.max(0, Math.min(maxScroll, dragStartScrollPos + scrollDelta));
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        isDraggingScrollbar = false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int scrollAmount = fontRendererObj.FONT_HEIGHT * 3;
            if (dWheel > 0) {
                scrollPos = Math.max(0, scrollPos - scrollAmount);
            } else {
                scrollPos = Math.min(maxScroll, scrollPos + scrollAmount);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Title
        drawCenteredString(this.fontRendererObj, translate("title"), this.width / 2, 20, 0xFFFFFF);

        // Search Field
        drawString(this.fontRendererObj, translate("search") + ":", searchField.xPosition - 45, 45, 0xA0A0A0);
        this.searchField.drawTextBox();

        // Headers
        int headerY = 70;
        int left = 20;
        drawString(this.fontRendererObj, translate("time"), left + 5, headerY, 0xFFFFFF);
        drawString(this.fontRendererObj, translate("packet_name"), left + 80, headerY, 0xFFFFFF);
        drawString(this.fontRendererObj, translate("packet_data"), left + 200, headerY, 0xFFFFFF);

        int top = 90;
        int bottom = this.height - 40;
        int right = this.width - 20;

        int yPos = top - scrollPos;

        // Entries
        for (int i = 0; i < filteredLogs.size(); i++) {
            DisplayPacketLogEntry entry = filteredLogs.get(i);
            int slotHeight = calculateEntryHeight(entry);

            if (yPos + slotHeight > top && yPos < bottom) {
                int bgColor = (i == selectedLine) ? 0x40FFFFFF : (i % 2 == 0 ? 0x20000000 : 0x10000000);
                drawRect(left, Math.max(yPos, top), right - 10, Math.min(yPos + slotHeight, bottom), bgColor);

                int textColor = (i == selectedLine) ? 0xFFFFE0 : 0xFFFFFF;

                // Time
                if (yPos + 2 >= top && yPos + 2 < bottom) {
                    drawString(this.fontRendererObj, entry.getFormattedTime(), left + 5, yPos + 2, textColor);
                }

                // Packet Name
                if (yPos + 2 >= top && yPos + 2 < bottom) {
                    int packetNameColor = entry.getDirection() == PacketDirection.SERVER ? 0x80FF80 : 0xFF8080;
                    drawString(this.fontRendererObj, entry.getDirection() == PacketDirection.SERVER ? "▼" : "▲", left + 73, yPos + 2, packetNameColor);
                    drawString(this.fontRendererObj, entry.getPacketName(), left + 80, yPos + 2, textColor);
                }

                // Packet Data Info
                if (entry.isExpanded()) {
                    List<String> lines = fontRendererObj.listFormattedStringToWidth(entry.getPacketData(), 280);
                    int lineY = yPos + 2;
                    for (String line : lines) {
                        if (lineY >= top && lineY + fontRendererObj.FONT_HEIGHT <= bottom) {
                            drawString(this.fontRendererObj, line, left + 200, lineY, textColor);
                        }
                        lineY += fontRendererObj.FONT_HEIGHT;
                    }
                } else {
                    if (yPos + 2 >= top && yPos + 2 < bottom) {
                        String abbreviated = entry.getPacketDataShort();
                        drawString(this.fontRendererObj, abbreviated, left + 200, yPos + 2, textColor);
                    }
                }
            }

            yPos += slotHeight;
            if (yPos > bottom + 100) break;
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
            int handleTop = top + (int) ((float) scrollPos / maxScroll * (scrollbarHeight - handleHeight));
            drawRect(scrollbarLeft, handleTop, right, handleTop + handleHeight, 0xFFC0C0C0);
        }
    }

    private int calculateEntryHeight(DisplayPacketLogEntry entry) {
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
            height += calculateEntryHeight(entry);
        }
        return Math.max(height, 1);
    }

    public void refreshLogs(List<DisplayPacketLogEntry> newLogs) {
        this.allLogs = newLogs;
        filterLogs();
    }

    public void refreshLogs() {
        filterLogs();
    }

    private void filterLogs() {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            filteredLogs = allLogs;
        } else {
            List<DisplayPacketLogEntry> filtered = new ArrayList<>(allLogs.size());

            for (DisplayPacketLogEntry log : allLogs) {
                if (log.getSearchTerms() != null) {
                    for (String searchTerm : log.getSearchTerms()) {
                        if (searchTerm.contains(searchText)) {
                            filtered.add(log);
                            break;
                        }
                    }
                }

            }

            filteredLogs = filtered;
        }

        updateMaxScroll();
        scrollPos = Math.min(scrollPos, maxScroll);
    }

    private void updateMaxScroll() {
        int visibleHeight = this.height - 130;
        int totalContentHeight = getTotalContentHeight();

        maxScroll = Math.max(0, totalContentHeight - visibleHeight);
    }

    private String translate(String key) {
        return translateToLocal("pdam.gui.packet_log." + key);
    }

}

