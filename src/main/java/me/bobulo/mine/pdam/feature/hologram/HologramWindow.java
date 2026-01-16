package me.bobulo.mine.pdam.feature.hologram;

import imgui.extension.imguizmo.flag.Mode;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.guizmo.GuizmoImGui;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static imgui.ImGui.*;

public final class HologramWindow extends AbstractRenderItemWindow {

    private static final Logger log = LogManager.getLogger(HologramWindow.class);

    private Hologram hologram;
    private final float[] position = new float[]{0f, 0f, 0f};

    private final ImString hologramText = new ImString(2048);
    private boolean updatedText = false;

    private final ImBoolean lockToView = new ImBoolean();
    private final ImFloat viewDistance = new ImFloat(5f);

    private final ImBoolean manipule = new ImBoolean();
    private final float[] manipulatePosition = new float[3];

    public HologramWindow() {
        super("Hologram Mockup");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(400, 525, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Hologram Mockup###HologramWindow", isVisible)) {
            renderContent();
        }

        end();

        if (isVisible.get()) {
            try {
                if (manipule.get() && !lockToView.get()) {
                    manipulatePosition[0] = position[0];
                    manipulatePosition[1] = position[1] + 2.3F; // Offset to match hologram height
                    manipulatePosition[2] = position[2];
                    GuizmoImGui.manipulateTranslation(Mode.WORLD, manipulatePosition);
                    position[0] = manipulatePosition[0];
                    position[1] = manipulatePosition[1] - 2.3F; // Revert offset
                    position[2] = manipulatePosition[2];
                }

                updateHologramPosition();

                if (updatedText) {
                    updateText();
                    updatedText = false;
                }
            } catch (Exception exception) {
                log.error("Error updating hologram", exception);
            }
        } else {
            if (hologram != null) {
                hologram.clear();
                hologram = null;
            }
        }
    }

    private void renderContent() {
        checkbox("Lock to view", lockToView);
        boolean lockToViewEnabled = lockToView.get();

        if (!lockToViewEnabled) {
            beginDisabled();
        }

        sameLine();
        setNextItemWidth(80.0f);
        inputFloat("Distance (blocks)", viewDistance);

        if (!lockToViewEnabled) {
            endDisabled();
        }

        if (lockToViewEnabled) {
            beginDisabled();
        }

        if (inputFloat3("Hologram Position", position)) {
            if (hologram != null) {
                hologram.setPosition(position[0], position[1], position[2]);
            }
        }

        checkbox("Show Movement Handles", manipule);
        if (isItemHovered()) {
            setTooltip("Enable to move the hologram using visual handles in the 3D world.");
        }

        if (lockToViewEnabled) {
            endDisabled();
        }

        separator();
        if (inputTextMultiline("##HologramText", hologramText, -1, -1)) {
            updatedText = true;
        }
    }

    private void updateText() {
        if (hologram == null) {
            return;
        }

        String[] split = hologramText.get()
          .replace("&", "§") // Support for & color codes
          .split("\n");

        hologram.setEntityLines(split);
    }

    private void updateHologramPosition() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        Vec3 positionVector = player.getPositionVector();
        if (positionVector == null) {
            return;
        }

        if (hologram == null) {
            hologram = new Hologram(positionVector.xCoord, positionVector.yCoord, positionVector.zCoord);
            position[0] = (float) positionVector.xCoord;
            position[1] = (float) positionVector.yCoord;
            position[2] = (float) positionVector.zCoord;
        }

        if (lockToView.get()) {
            float viewDistanceBlocks = viewDistance.get();

            Vec3 eyePos = player.getPositionEyes(1.0F);
            Vec3 look = player.getLookVec();
            Vec3 target = eyePos.addVector(
              look.xCoord * viewDistanceBlocks,
              look.yCoord * viewDistanceBlocks - (hologram.getLineCount() * 0.25f) / 2f - 2, // Center the hologram vertically and offset down
              look.zCoord * viewDistanceBlocks
            );

            hologram.setPosition((float) target.xCoord, (float) target.yCoord, (float) target.zCoord);
            position[0] = (float) target.xCoord;
            position[1] = (float) target.yCoord;
            position[2] = (float) target.zCoord;
            return;
        }

        hologram.setPosition(position[0], position[1], position[2]);
    }

}
