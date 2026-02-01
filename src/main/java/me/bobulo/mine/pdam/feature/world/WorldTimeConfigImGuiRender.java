package me.bobulo.mine.pdam.feature.world;

import imgui.flag.ImGuiSliderFlags;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import net.minecraft.client.Minecraft;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class WorldTimeConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    private final int[] timeOffsetCache = new int[]{WorldTime.WORLD_TIME.get()};

    @Override
    public void draw() {
        text("Current World Time: " + (Minecraft.getMinecraft().theWorld != null
          ? Minecraft.getMinecraft().theWorld.getWorldTime() : "N/A"));

        if (sliderInt("World Time", timeOffsetCache, 0, 24000, ImGuiSliderFlags.AlwaysClamp)) {
            WorldTime.WORLD_TIME.set(timeOffsetCache[0]);
            updateTime();
        }

        tooltip("Set the time of day in the Minecraft world.");

        if (button("Sunrise")) {
            setWorldTime(0);
        }
        sameLine();
        if (button("Noon")) {
            setWorldTime(6000);
            updateTime();
        }
        sameLine();
        if (button("Sunset")) {
            setWorldTime(12000);
            updateTime();
        }
        sameLine();
        if (button("Midnight")) {
            setWorldTime(18000);
        }
    }

    private void setWorldTime(int time) {
        timeOffsetCache[0] = time;
        WorldTime.WORLD_TIME.set(time);
        updateTime();
    }

    private void updateTime() {
        if (!WorldTime.isFeatureEnabled()) {
            return;
        }

        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.provider.setWorldTime(timeOffsetCache[0]);
        }
    }

}
