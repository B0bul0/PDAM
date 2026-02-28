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
import me.bobulo.mine.pdam.util.BlockPosition;
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
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;
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

    private final ImBoolean paused = new ImBoolean(false);
    private List<WorldSign> cachedSigns = Collections.emptyList();

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
        if (button(paused.get() ? "Resume" : "Pause")) {
            paused.set(!paused.get());
        }

        separator();

        List<WorldSign> displayedSigns = findFilteredSigns();

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
            tableSetupColumn("Actions", ImGuiTableColumnFlags.WidthFixed, 120F);
            tableHeadersRow();

            float rowHeight = getTextLineHeight() * 4F;
            clipper.begin(displayedSigns.size(), rowHeight);

            while (clipper.step()) {
                for (int index = clipper.getDisplayStart(); index < clipper.getDisplayEnd(); index++) {
                    if (index >= displayedSigns.size()) break;
                    WorldSign foundSign = displayedSigns.get(index);

                    String signText = Stream.of(foundSign.getText())
                      .map(text -> text == null || text.isEmpty() ? "\u00A0" : text)
                      .collect(Collectors.joining("\n"));

                    pushID("SignRow" + index);

                    int signX = foundSign.getPosition().getX();
                    int signY = foundSign.getPosition().getY();
                    int signZ = foundSign.getPosition().getZ();
                    double distance = player.getDistance(signX, signY, signZ);

                    tableNextRow(rowHeight);
                    tableNextColumn();

                    int signHash = foundSign.getPosition().hashCode();
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
                                WorldSign signInRange = displayedSigns.get(i);
                                int rangeSignHash = signInRange.getPosition().hashCode();
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

    private List<WorldSign> findFilteredSigns() {
        List<WorldSign> foundSigns = paused.get() ? cachedSigns : findWorldSigns();

        List<WorldSign> displayedSigns = new ArrayList<>(foundSigns.size());
        for (WorldSign foundSign : foundSigns) {
            String signText = String.join("\n", foundSign.getText());

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

    private List<WorldSign> findWorldSigns() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) {
            return Collections.emptyList();
        }

        List<WorldSign> foundSigns = new ArrayList<>(mc.theWorld.loadedTileEntityList.size() / 3);

        for (TileEntity te : mc.theWorld.loadedTileEntityList) {
            if (te instanceof TileEntitySign) {
                IChatComponent[] signText = ((TileEntitySign) te).signText;

                WorldSign sign = new WorldSign(
                  new String[]{
                    signText[0].getUnformattedText(),
                    signText[1].getUnformattedText(),
                    signText[2].getUnformattedText(),
                    signText[3].getUnformattedText()
                  },
                  BlockPosition.from(te.getPos())
                );

                foundSigns.add(sign);
            }
        }

        cachedSigns = foundSigns; // cache for pause functionality
        return foundSigns;
    }

    private EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

}
