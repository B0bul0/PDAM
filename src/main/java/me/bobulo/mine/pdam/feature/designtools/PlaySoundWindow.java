package me.bobulo.mine.pdam.feature.designtools;

import com.google.common.collect.ImmutableList;
import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import me.bobulo.mine.pdam.feature.sound.window.SoundMapperDrawer;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.UniqueHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.*;

public final class PlaySoundWindow extends AbstractRenderItemWindow {

    private static final Random RANDOM = new Random();
    private static final String NONE_SOUND = "none";

    // Cache of sound event locations for filtering and selection
    private boolean initialized = false;
    private SoundRegistry soundRegistry;
    private List<ResourceLocation> soundEventLocations;

    private final SoundMapperDrawer soundMapper = new SoundMapperDrawer();

    // Sound playing controls
    private String soundToPlay = NONE_SOUND;
    private final float[] pitch = new float[]{1.0f};

    private final ImGuiTextFilter soundFilter = new ImGuiTextFilter();
    private final UniqueHistory<PlaySoundEntry> playSoundEntries = new UniqueHistory<>(25);

    public PlaySoundWindow() {
        super("Play Sound");
    }

    private void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        this.soundRegistry = ObfuscationReflectionHelper.getPrivateValue(
          SoundHandler.class,
          Minecraft.getMinecraft().getSoundHandler(),
          "sndRegistry",
          "field_147697_e"
        );

        // Cache sound event location
        if (soundRegistry != null) {
            this.soundEventLocations = ImmutableList.copyOf(soundRegistry.getKeys());
        } else {
            this.soundEventLocations = ImmutableList.of();
        }
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
        initialize();

        if (soundRegistry == null) {
            text("Sound registry not available.");
            end();
            return;
        }

        soundMapper.draw();

        separator();

        renderPlaySound();

        end();
    }

    private void renderPlaySound() {
        Minecraft mc = Minecraft.getMinecraft();

        text("Play sound");

        if (beginCombo("Select Sound", mapSoundName(soundToPlay))) {

            if (isWindowAppearing()) {
                setKeyboardFocusHere();
                soundFilter.clear();
            }

            setNextItemShortcut(ImGuiKey.ModCtrl | ImGuiKey.F);

            if (soundFilter.draw("Filter")) {
                soundToPlay = NONE_SOUND;
            }

            for (ResourceLocation soundEventLocation : soundEventLocations) {
                String soundName = mapSoundName(soundEventLocation.toString());

                if (soundFilter.passFilter(soundName) && selectable(soundName, soundName.equals(soundToPlay))) {
                    soundToPlay = soundEventLocation.toString();
                }
            }

            endCombo();
        }

        // 0.5x to 2x pitch range matching Minecraft's sound pitch limits
        sliderFloat("Pitch", pitch, 0.5f, 2.0f);

        text("Play Test Sound:");
        sameLine();
        if (button("Current Sound")) {
            playSelectedSound(mc);
        }

        verticalSeparator();

        sameLine();
        if (button("Next Sound")) {
            String currentSound = soundToPlay;
            boolean foundCurrent = false;
            String nextSound = null;

            for (ResourceLocation soundEventLocation : soundEventLocations) {
                String soundName = soundEventLocation.toString();

                if (foundCurrent) {
                    nextSound = soundName;
                    break;
                } else if (soundName.equals(currentSound) || NONE_SOUND.equals(currentSound)) {
                    foundCurrent = true;
                }
            }

            soundToPlay = nextSound == null ? NONE_SOUND : nextSound;
            playSelectedSound(mc);
        }

        tooltip("Play the next sound in the list after the currently selected one.");

        sameLine();
        if (button("Random Sound")) {
            int randomIndex = RANDOM.nextInt(soundEventLocations.size());
            soundToPlay = soundEventLocations.get(randomIndex).toString();
            playSelectedSound(mc);
        }

        tooltip("Play a random sound from the registry.");

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
                if (smallButton("Play##" + entry.soundName + "_" + entry.pitch)) {
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

    private void playSelectedSound(Minecraft mc) {
        if (soundToPlay == null || soundToPlay.isEmpty() || NONE_SOUND.equals(soundToPlay)) {
            return;
        }

        String originalSound = soundToPlay;

        mc.getSoundHandler().playSound(
          PositionedSoundRecord.create(
            new ResourceLocation(originalSound), pitch[0]
          )
        );

        playSoundEntries.push(new PlaySoundEntry(originalSound, pitch[0]));
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