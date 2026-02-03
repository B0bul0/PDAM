package me.bobulo.mine.pdam.feature.signfinder;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import imgui.ImGuiListClipper;
import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImBoolean;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;
import static me.bobulo.mine.pdam.util.ClipboardUtils.copyToClipboard;

/**
 * A GUI window that allows players to find signs in their Minecraft world.
 */
public final class SignFinderWindow extends AbstractRenderItemWindow {

    // Filtering options
    private final ImGuiTextFilter filter = new ImGuiTextFilter();
    private final ImBoolean ignoreEmptySigns = new ImBoolean(true);

    // List clipping for performance
    private final ImGuiListClipper clipper = new ImGuiListClipper();

    // Multi-selection
    private final TIntSet selectedSignHashes = new TIntHashSet();
    private int lastSelectedSignIndex = 0;

    public SignFinderWindow() {
        super("Sign Finder");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(400, 400, ImGuiCond.FirstUseEver);
        setNextWindowPos(30, 30, ImGuiCond.FirstUseEver);

        if (begin("Sign Finder##SignFinderWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        EntityPlayerSP player = getPlayer();
        if (Minecraft.getMinecraft().theWorld == null) {
            text("No world loaded.");
            return;
        }

        text("Filter Signs Text");
        sameLine();
        filter.draw("##FilterSignsText");
        checkbox("Ignore Empty Signs", ignoreEmptySigns);
        separator();

        List<TileEntitySign> displayedSigns = findFilteredSigns();

        text("Found Signs: (" + displayedSigns.size() + ")");

        int flags = ImGuiTableFlags.Borders
          | ImGuiTableFlags.RowBg
          | ImGuiTableFlags.ScrollY
          | ImGuiTableFlags.Resizable
          | ImGuiTableFlags.Hideable;

        if (beginTable("FoundSignsTable", 4, flags)) {
            tableSetupColumn("Text", ImGuiTableColumnFlags.WidthStretch);
            tableSetupColumn("Distance", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Location", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Actions", ImGuiTableColumnFlags.WidthFixed);
            tableHeadersRow();

            float rowHeight = getTextLineHeight() * 4F;
            clipper.begin(displayedSigns.size(), rowHeight);

            while (clipper.step()) {
                for (int index = clipper.getDisplayStart(); index < clipper.getDisplayEnd(); index++) {
                    if (index >= displayedSigns.size()) break;
                    TileEntitySign foundSign = displayedSigns.get(index);

                    String signText = Stream.of(foundSign.signText)
                      .map(component -> {
                          String text = component == null ? "" : component.getUnformattedText();
                          return text == null || text.isEmpty() ? "\u00A0" : text;
                      })
                      .collect(Collectors.joining("\n"));

                    pushID("SignRow" + index);

                    int signX = foundSign.getPos().getX();
                    int signY = foundSign.getPos().getY();
                    int signZ = foundSign.getPos().getZ();
                    double distance = player.getDistance(signX, signY, signZ);

                    tableNextRow(rowHeight);
                    tableNextColumn();

                    int signHash = foundSign.getPos().hashCode();
                    if (selectable(signText, selectedSignHashes.contains(signHash),
                      ImGuiSelectableFlags.SpanAllColumns | ImGuiSelectableFlags.AllowItemOverlap)) {
                        if (getIO().getKeyCtrl()) {
                            // Toggle selection on Ctrl
                            if (selectedSignHashes.contains(signHash)) {
                                selectedSignHashes.remove(signHash);
                                SignESP.renderingSigns.remove(foundSign);
                            } else {
                                selectedSignHashes.add(signHash);
                                SignESP.renderingSigns.add(foundSign);
                            }

                            lastSelectedSignIndex = index;
                        } else if (getIO().getKeyShift()) {
                            // Range selection on Shift
                            int start = Math.min(lastSelectedSignIndex, index);
                            int end = Math.max(lastSelectedSignIndex, index);
                            selectedSignHashes.clear();
                            SignESP.renderingSigns.clear();

                            for (int i = start; i <= end; i++) {
                                TileEntitySign signInRange = displayedSigns.get(i);
                                int rangeSignHash = signInRange.getPos().hashCode();
                                selectedSignHashes.add(rangeSignHash);
                                SignESP.renderingSigns.add(signInRange);
                            }
                        } else {
                            selectedSignHashes.clear();
                            SignESP.renderingSigns.clear();

                            // Select only this sign
                            selectedSignHashes.add(signHash);
                            SignESP.renderingSigns.add(foundSign);
                            lastSelectedSignIndex = index;
                        }

                    }

                    tableNextColumn();
                    text(String.format("%.2f", distance));

                    tableNextColumn();
                    text(String.format("%d, %d, %d", signX, signY, signZ));

                    tableNextColumn();
                    if (button("Teleport")) {
                        PlayerUtils.teleportViaServer(signX, signY, signZ);
                    }
                    if (button("Copy Coords")) {
                        copyToClipboard(String.format("%d, %d, %d", signX, signY, signZ));
                    }

                    popID();
                }
            }

            if (getScrollY() >= getScrollMaxY()) {
                setScrollHereY(1.0f);
            }

            clipper.end();
            endTable();
        }

    }

    private List<TileEntitySign> findFilteredSigns() {
        List<TileEntitySign> foundSigns = findWorldSigns();

        List<TileEntitySign> displayedSigns = new ArrayList<>(foundSigns.size());
        for (TileEntitySign foundSign : foundSigns) {
            String signText = Stream.of(foundSign.signText)
              .map(IChatComponent::getUnformattedText)
              .collect(Collectors.joining("\n"));

            if (ignoreEmptySigns.get() && signText.trim().isEmpty()) {
                continue;
            }

            if (!filter.passFilter(signText)) {
                continue;
            }

            displayedSigns.add(foundSign);
        }

        return displayedSigns;
    }

    private List<TileEntitySign> findWorldSigns() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) {
            return Collections.emptyList();
        }

        List<TileEntitySign> foundSigns = new ArrayList<>(mc.theWorld.loadedTileEntityList.size() / 3);

        for (TileEntity te : mc.theWorld.loadedTileEntityList) {
            if (te instanceof TileEntitySign) {
                foundSigns.add((TileEntitySign) te);
            }
        }

        return foundSigns;
    }

    private EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

}
