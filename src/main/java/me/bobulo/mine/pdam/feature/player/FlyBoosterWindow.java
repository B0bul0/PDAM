package me.bobulo.mine.pdam.feature.player;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiSliderFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

/**
 * A GUI window that allows the player to boost their flying speed in Minecraft.
 */
public class FlyBoosterWindow extends AbstractRenderItemWindow {

    private static final float DEFAULT_FLY_SPEED = 0.05f;

    private final ImBoolean enableFlySpeed = new ImBoolean(false);
    private final float[] flySpeed = new float[]{0.1f};

    public FlyBoosterWindow() {
        super("Fly Booster");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(235, 100, ImGuiCond.Always);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Fly Booster##FlyBoosterWindow", isVisible, ImGuiWindowFlags.NoResize)) {
            keepInScreen();
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

        sliderFloat("Fly Speed", flySpeed, 0.0f, 1.0f, "%.2f", ImGuiSliderFlags.AlwaysClamp);

        drawSpeedButtonMultiplier(0.5f);
        sameLine();

        for (int i = 0; i < 4; i++) {
            drawSpeedButtonMultiplier(i + 1f);
            if (i < 3) {
                sameLine();
            }
        }

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

    private void drawSpeedButtonMultiplier(float multiplier) {
        if (button("x" + multiplier)) {
            flySpeed[0] = DEFAULT_FLY_SPEED * multiplier;
        }
        tooltip("Set fly speed to " + (DEFAULT_FLY_SPEED * multiplier));
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
