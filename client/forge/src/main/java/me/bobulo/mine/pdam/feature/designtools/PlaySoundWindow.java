package me.bobulo.mine.pdam.feature.designtools;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.flag.*;
import me.bobulo.mine.pdam.feature.sound.window.SoundMapperDrawer;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.UniqueHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Objects;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;

public final class PlaySoundWindow extends AbstractRenderItemWindow {

    private final SoundMapperDrawer soundMapper = new SoundMapperDrawer();

    // Sound playing controls
    private String soundToPlay = "none";
    private final float[] pitch = new float[]{1.0f};
    private final ImGuiTextFilter soundFilter = new ImGuiTextFilter();
    private final UniqueHistory<PlaySoundEntry> playSoundEntries = new UniqueHistory<>(25);

    public PlaySoundWindow() {
        super("Play Sound");
    }

    @Override
    public void renderGui() {
        ImGui.setNextWindowSize(600, 400, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowPos(50, 60, ImGuiCond.FirstUseEver);

        if (!begin("Play Sound##PlaySoundWindow", isVisible)) {
            end();
            return;
        }

        keepInScreen();

        Minecraft mc = Minecraft.getMinecraft();

        SoundRegistry soundRegistry = ObfuscationReflectionHelper.getPrivateValue(
          SoundHandler.class,
          mc.getSoundHandler(),
          "sndRegistry",
          "field_147697_e"
        );

        soundMapper.draw();

        separator();

        renderPlaySound(mc, soundRegistry);

        end();
    }

    private void renderPlaySound(Minecraft mc, SoundRegistry soundRegistry) {
        if (soundRegistry == null) {
            text("Sound registry not available.");
            return;
        }

        text("Play sound");

        if (beginCombo("Select Sound", soundToPlay)) {

            if (isWindowAppearing()) {
                setKeyboardFocusHere();
                soundFilter.clear();
            }

            setNextItemShortcut(ImGuiKey.ModCtrl | ImGuiKey.F);

            if (soundFilter.draw("Filter")) {
                soundToPlay = "none";
            }

            for (SoundEventAccessorComposite soundEventAccessorComposite : soundRegistry) {
                ResourceLocation soundEventLocation = soundEventAccessorComposite.getSoundEventLocation();
                String soundName = mapSoundName(soundEventLocation.toString());

                if (soundFilter.passFilter(soundName) && selectable(soundName, soundName.equals(soundToPlay))) {
                    soundToPlay = soundName;
                }
            }
            endCombo();
        }

        sliderFloat("Pitch", pitch, 0.5f, 2.0f);

        if (button("Play Test Sound")) {
            if (!"none".equals(soundToPlay)) {
                String originalSound = reverseMapSoundName(soundToPlay);

                mc.getSoundHandler().playSound(
                  PositionedSoundRecord.create(
                    new ResourceLocation(originalSound), pitch[0]
                  )
                );

                playSoundEntries.push(new PlaySoundEntry(originalSound, pitch[0]));
            }
        }

        separator();

        // Table history
        text("Tested Sounds:");
        int flags = ImGuiTableFlags.Borders
          | ImGuiTableFlags.RowBg
          | ImGuiTableFlags.ScrollY;

        if (beginTable("TestedSoundsTable", 3, flags)) {
            tableSetupColumn("Sound Name", ImGuiTableColumnFlags.WidthStretch);
            tableSetupColumn("Pitch", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Play", ImGuiTableColumnFlags.WidthFixed);
            tableHeadersRow();

            for (PlaySoundEntry entry : playSoundEntries) {
                tableNextRow();
                tableNextColumn();
                text(mapSoundName(entry.soundName));
                tableNextColumn();
                text(String.format("%.2f", entry.pitch));
                tableNextColumn();
                if (button("Play##" + entry.soundName + "_" + entry.pitch)) {
                    mc.getSoundHandler().playSound(
                      PositionedSoundRecord.create(
                        new ResourceLocation(entry.soundName), entry.pitch
                      )
                    );
                }
            }

            endTable();
        }
    }

    private String mapSoundName(String vanillaName) {
        return soundMapper.mapSoundName(vanillaName);
    }

    private String reverseMapSoundName(String mappedName) {
        return soundMapper.reverseMapSoundName(mappedName);
    }

    static class PlaySoundEntry {
        String soundName;
        float pitch;

        PlaySoundEntry(String soundName, float pitch) {
            this.soundName = soundName;
            this.pitch = pitch;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PlaySoundEntry)) return false;
            PlaySoundEntry that = (PlaySoundEntry) o;
            return Float.compare(pitch, that.pitch) == 0 && Objects.equals(soundName, that.soundName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(soundName, pitch);
        }
    }

}