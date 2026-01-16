package me.bobulo.mine.pdam.feature.player;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import static imgui.ImGui.*;

/**
 * A GUI window that allows the player to boost their flying speed in Minecraft.
 */
public class FlyBoosterWindow extends AbstractRenderItemWindow {

    private final ImBoolean enableFlySpeed = new ImBoolean(false);
    private final float[] flySpeed = new float[]{0.1f};

    public FlyBoosterWindow() {
        super("Fly Booster");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(235, 80, ImGuiCond.Always);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Fly Booster###FlyBoosterWindow", isVisible, ImGuiWindowFlags.NoResize)) {
            renderContent();
        }

        end();

        // Auto disable when window is closed
        if (!isVisible.get() && enableFlySpeed.get()) {
            cancel();
        }

    }

    private void renderContent() {
        EntityPlayerSP player = getPlayer();
        boolean playerInGame = player != null;
        if (!playerInGame) {
            beginDisabled();
        }

        if (player == null && enableFlySpeed.get()) {
            cancel(); // Disable if player is not in game
        }

        checkbox("Enable Fly Speed", enableFlySpeed);
        if (isItemHovered()) {
            setTooltip("This may be considered a cheat. Use only with server permission.");
        }

        if (playerInGame && !enableFlySpeed.get()) {
            beginDisabled();
            flySpeed[0] = player.capabilities.getFlySpeed();
        }

        sameLine();

        if (button("Reset")) {
            reset();
        }

        sliderFloat("Fly Speed", flySpeed, 0.0f, 1.0f);

        if (playerInGame) {
            if (enableFlySpeed.get()) {
                refreshFlySpeed();
            } else {
                endDisabled();
                cancel();
            }
        }

        if (!playerInGame) {
            endDisabled();
        }

    }

    private void reset() {
        flySpeed[0] = 0.05f;
        refreshFlySpeed();
    }

    private void cancel() {
        enableFlySpeed.set(false);
        reset();
    }

    private void refreshFlySpeed() {
        setFlySpeed(flySpeed[0]);
    }

    private void setFlySpeed(float speed) {
        EntityPlayerSP player = getPlayer();
        if (player != null) {
            player.capabilities.setFlySpeed(speed);
        }
    }

    private EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

}
