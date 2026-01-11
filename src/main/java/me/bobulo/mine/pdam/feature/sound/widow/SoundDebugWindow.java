package me.bobulo.mine.pdam.feature.sound.widow;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import me.bobulo.mine.pdam.feature.sound.SoundDebugFeatureComponent;
import me.bobulo.mine.pdam.feature.sound.log.SoundLogEntry;
import me.bobulo.mine.pdam.feature.sound.mapper.SoundMapper;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.imgui.window.LogWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.*;

import static imgui.ImGui.*;

public class SoundDebugWindow extends AbstractRenderItemWindow {

    private final SoundDebugFeatureComponent feature;

    private SoundMapper soundMapper = SoundMapper.VANILLA;

    // Sound playing controls
    private String soundToPlay = "none";
    private final float[] pitch = new float[]{1.0f};
    private final ImGuiTextFilter soundFilter = new ImGuiTextFilter();

    private final LogWindow<SoundLogEntry> logWindow;

    private final UniqueHistory<PlaySoundEntry> playSoundEntries = new UniqueHistory<>(25);

    public SoundDebugWindow(SoundDebugFeatureComponent feature) {
        super("Sound Debugger");
        this.feature = feature;
        this.logWindow = new LogWindow<>(feature.getSoundHistory(), entry -> {
            String soundName = mapSoundName(entry.getSoundName());
            return String.format("Sound: %s | Volume: %.2f | Pitch: %.2f | Location: (%.2f, %.2f, %.2f)",
              soundName,
              entry.getVolume(),
              entry.getPitch(),
              entry.getX(),
              entry.getY(),
              entry.getZ()
            );
        });
    }

    @Override
    public void renderGui() {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowPos(50, 60, ImGuiCond.FirstUseEver);

        if (!begin("Sound Debugger##SoundDebug", isVisible)) {
            end();
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        SoundManager soundManager = ObfuscationReflectionHelper.getPrivateValue(
          SoundHandler.class,
          mc.getSoundHandler(),
          "sndManager",
          "field_147694_f"
        );

        SoundRegistry soundRegistry = ObfuscationReflectionHelper.getPrivateValue(
          SoundHandler.class,
          mc.getSoundHandler(),
          "sndRegistry",
          "field_147697_e"
        );

        Map<String, ISound> playingSounds = ObfuscationReflectionHelper.getPrivateValue(
          SoundManager.class,
          soundManager,
          "playingSounds",
          "field_148629_h"
        );

        text("Sound Name Mapping:");
        if (radioButton("Vanilla", soundMapper == SoundMapper.VANILLA)) {
            soundMapper = SoundMapper.VANILLA;
        }
        sameLine();
        if (radioButton("Bukkit 1.8", soundMapper == SoundMapper.BUKKIT_1_8)) {
            soundMapper = SoundMapper.BUKKIT_1_8;
        }

        separator();

        // Abas para separar as seções
        if (beginTabBar("SoundTabs")) {
            if (beginTabItem("Active Sounds")) {
                renderActiveSounds(playingSounds);
                endTabItem();
            }

            if (beginTabItem("Play Sound")) {
                renderPlaySound(mc, soundRegistry);
                endTabItem();
            }

            if (beginTabItem("Log Sounds")) {
                logWindow.newFrame();
                endTabItem();
            }

            endTabBar();
        }

        end();
    }

    private void renderActiveSounds(Map<String, ISound> playingSounds) {
        if (playingSounds == null) {
            text("Playing sounds not available.");
            return;
        }

        int flags = ImGuiTableFlags.Borders
          | ImGuiTableFlags.RowBg
          | ImGuiTableFlags.ScrollY;

        text("Active Sounds: " + playingSounds.size());
        separator();
        if (beginTable("PacketLogsTable", 4, flags)) {
            tableSetupColumn("Sound Name", ImGuiTableColumnFlags.WidthStretch);
            tableSetupColumn("Volume", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Pitch", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Location", ImGuiTableColumnFlags.WidthFixed, 250F);
            tableHeadersRow();

            for (ISound sound : playingSounds.values()) {
                tableNextRow();
                tableNextColumn();

                String soundName = mapSoundName(sound.getSoundLocation().toString());

                text(soundName);
                tableNextColumn();
                text(String.format("%.2f", sound.getVolume()));
                tableNextColumn();
                text(String.format("%.2f", sound.getPitch()));
                tableNextColumn();
                if (sound instanceof PositionedSound) {
                    PositionedSound positionedSound = (PositionedSound) sound;
                    text(String.format("X: %.2f Y: %.2f Z: %.2f",
                      positionedSound.getXPosF(),
                      positionedSound.getYPosF(),
                      positionedSound.getZPosF()
                    ));
                } else {
                    text("N/A");
                }

            }

            endTable();
        }

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
        if (soundMapper == null) {
            return vanillaName;
        }

        String mappedName = soundMapper.mapSoundName(vanillaName);
        return mappedName != null ? mappedName : vanillaName;
    }

    private String reverseMapSoundName(String mappedName) {
        if (soundMapper == null) {
            return mappedName;
        }

        String vanillaName = soundMapper.reverseMapSoundName(mappedName);
        return vanillaName != null ? vanillaName : mappedName;
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