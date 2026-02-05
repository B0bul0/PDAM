package me.bobulo.mine.pdam.feature.player;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.notification.Notifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;
import static me.bobulo.mine.pdam.util.LocaleUtils.translateToLocal;

/**
 * A GUI window that displays the player's coordinates.
 */
public final class CoordinatesWindow extends AbstractRenderItemWindow {

    public CoordinatesWindow() {
        super("Player Coordinates");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(215, 99, ImGuiCond.Always);
        setNextWindowPos(30, 30, ImGuiCond.FirstUseEver);

        if (begin("Player Coordinates##CoordinatesWindow", isVisible, ImGuiWindowFlags.NoResize)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        EntityPlayerSP player = getPlayer();
        if (player == null) {
            text("No player found.");
            return;
        }

        BlockPos pos = player.getPosition();

        copyableLabel("Location:", String.format("%.2f %.2f %.2f", player.posX, player.posY, player.posZ));
        sameLine();
        drawCoord("X", player.posX);
        sameLine();
        drawCoord("Y", player.posY);
        sameLine();
        drawCoord("Z", player.posZ);

        copyableLabel("Pitch/Yaw:", String.format("%.2f %.2f", player.rotationPitch, player.rotationYaw));
        sameLine();
        drawCoord("Pitch", player.rotationPitch);
        sameLine();
        drawCoord("Yaw", player.rotationYaw);

        copyableLabel("Block Pos:", String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()));
        sameLine();
        sameLine();
        drawCoord("Block X", pos.getX());
        sameLine();
        drawCoord("Block Y", pos.getY());
        sameLine();
        drawCoord("Block Z", pos.getZ());

        textColored(0.7f, 0.7f, 0.7f, 1.0f, "Chunk:");
        sameLine();
        text(String.format("%d %d", pos.getX() >> 4, pos.getZ() >> 4));
        tooltip("Chunk X Z");

        sameLine();
        textColored(0.7f, 0.7f, 0.7f, 1.0f, "Section:");
        sameLine();
        drawCoord("Section Y", pos.getY() >> 4);
    }

    private void copyableLabel(String label, String textToCopy) {
        textColored(0.7f, 0.7f, 0.7f, 1.0f, label); // Gray label
        tooltip(label + "\nClick to copy: " + textToCopy);
        if (isItemClicked()) {
            setClipboardText(textToCopy);
            Notifier.showSuccess(translateToLocal("pdam.general.copied_to_clipboard"));
        }
    }

    private void drawCoord(String label, double value) {
        text(String.format("%.2f", value));
        tooltip(label);
        if (isItemClicked()) {
            setClipboardText(String.format("%.2f", value));
            Notifier.showSuccess(translateToLocal("pdam.general.copied_to_clipboard"));
        }
    }

    private void drawCoord(String label, int value) {
        text(String.format("%d", value));
        tooltip(label);
        if (isItemClicked()) {
            setClipboardText(String.format("%d", value));
            Notifier.showSuccess(translateToLocal("pdam.general.copied_to_clipboard"));
        }
    }

    private EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

}
