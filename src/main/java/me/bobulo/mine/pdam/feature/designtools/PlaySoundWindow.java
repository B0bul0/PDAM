package me.bobulo.mine.pdam.feature.designtools;

import com.google.common.collect.ImmutableList;
import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.flag.*;
import me.bobulo.mine.pdam.feature.sound.window.SoundMapperDrawer;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.UniqueHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.*;
import static net.minecraft.client.audio.SoundCategory.RECORDS;

public final class PlaySoundWindow extends AbstractRenderItemWindow {

    private static final Random RANDOM = new Random();
    private static final String NONE_SOUND = "none";

    // Cache of sound event locations for filtering and selection
    private boolean initialized = false;
    private SoundRegistry soundRegistry;
    private List<SoundEventAccessorComposite> soundEventLocations;

    private final SoundMapperDrawer soundMapper = new SoundMapperDrawer();

    // Sound playing controls
    private String soundToPlay = NONE_SOUND;
    private final float[] pitch = new float[]{1.0f};
    private final float[] volume = new float[]{1.0f};

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
            this.soundEventLocations = ImmutableList.copyOf(soundRegistry.iterator());
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
        separator();
        renderTestedSound();

        end();
    }

    private void renderPlaySound() {
        Minecraft mc = Minecraft.getMinecraft();

        text("Play sound");

        int beginComboFlags = ImGuiComboFlags.HeightLarge;
        if (beginCombo("Select Sound", mapSoundName(soundToPlay), beginComboFlags)) {

            if (isWindowAppearing()) {
                setKeyboardFocusHere();
                soundFilter.clear();
            }

            setNextItemShortcut(ImGuiKey.ModCtrl | ImGuiKey.F);

            if (soundFilter.draw("Filter")) {
                soundToPlay = NONE_SOUND;
            }

            for (SoundEventAccessorComposite soundEventLocation : soundEventLocations) {
                String originalName = soundEventLocation.getSoundEventLocation().toString();
                String displayName = mapSoundName(originalName);

                if (soundFilter.passFilter(displayName) &&
                  selectable(displayName, originalName.equals(soundToPlay))) {
                    soundToPlay = originalName;
                }
            }

            endCombo();
        }

        // 0.5x to 2x pitch range matching Minecraft's sound pitch limits
        sliderFloat("Pitch", pitch, 0.5f, 2.0f);
        tooltip("Pitch 1.0 is normal, less than 1.0 is lower pitch, greater than 1.0 is higher pitch.");

        // 0.0 to 1.0 volume range - over 1.0f doesn't increase volume, just allows sound to be heard from farther away
        sliderFloat("Volume", volume, 0.0f, 1.0f);
        tooltip("Volume 0.0 is silent, 1.0 is normal volume. " +
          "Values above 1.0 will not increase volume but allow the sound to be heard from farther away.");

        spacing();

        text("Play Test Sound:");
        sameLine();
        if (button("Current Sound")) {
            playSelectedSound();
        }

        verticalSeparator();

        sameLine();
        if (button("Next Sound")) {
            String currentSound = soundToPlay;
            boolean foundCurrent = false;
            String nextSound = null;

            for (SoundEventAccessorComposite soundEventLocation : soundEventLocations) {
                if (soundEventLocation.getSoundCategory() == SoundCategory.RECORDS ||
                  soundEventLocation.getSoundCategory() == SoundCategory.MUSIC) {
                    continue; // Skip music and record sounds to avoid long-playing audio during testing
                }

                String soundName = soundEventLocation.getSoundEventLocation().toString();

                if (foundCurrent) {
                    nextSound = soundName;
                    break;
                } else if (soundName.equals(currentSound) || NONE_SOUND.equals(currentSound)) {
                    foundCurrent = true;
                }
            }

            soundToPlay = nextSound == null ? NONE_SOUND : nextSound;
            playSelectedSound();
        }

        tooltip("Play the next sound in the list after the currently selected one.");

        sameLine();
        if (button("Random Sound")) {
            List<SoundEventAccessorComposite> list = soundEventLocations.stream()
              .filter(loc ->
                loc.getSoundCategory() != RECORDS &&
                  loc.getSoundCategory() != SoundCategory.MUSIC
              )
              .collect(Collectors.toList());

            float randomPitch = 0.5f + RANDOM.nextFloat() * 1.5f; // Random pitch between 0.5 and 2.0
            pitch[0] = randomPitch;

            if (list.isEmpty()) { // no valid sounds to play
                soundToPlay = NONE_SOUND;
            } else {
                int randomIndex = RANDOM.nextInt(list.size());
                soundToPlay = list.get(randomIndex).getSoundEventLocation().toString();
                playSelectedSound();
            }

        }

        tooltip("Play a random sound from the registry.");
    }

    private void renderTestedSound() {
        Minecraft mc = Minecraft.getMinecraft();

        // Table history
        text("Tested Sounds:");
        int flags = ImGuiTableFlags.Borders
          | ImGuiTableFlags.RowBg
          | ImGuiTableFlags.ScrollY;

        if (beginTable("TestedSoundsTable", 4, flags)) {
            tableSetupColumn("Sound Name", ImGuiTableColumnFlags.WidthStretch);
            tableSetupColumn("Pitch", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Volume", ImGuiTableColumnFlags.WidthFixed);
            tableSetupColumn("Play", ImGuiTableColumnFlags.WidthFixed);
            tableHeadersRow();

            int indexEntry = 0;
            for (PlaySoundEntry entry : playSoundEntries) {
                tableNextRow();
                tableNextColumn();
                text(mapSoundName(entry.soundName));
                tableNextColumn();
                text(String.format("%.2f", entry.pitch));
                tableNextColumn();
                text(String.format("%.2f", entry.volume));
                tableNextColumn();
                if (smallButton("Play##PlaySound" + indexEntry)) {
                    playSound(entry.soundName, entry.pitch, entry.volume);
                }

                indexEntry++;
            }

            endTable();
        }
    }

    private void playSelectedSound() {
        if (soundToPlay == null || soundToPlay.isEmpty() || NONE_SOUND.equals(soundToPlay)) {
            return;
        }

        String originalSound = soundToPlay;

        playSound(originalSound, pitch[0], volume[0]);
        playSoundEntries.push(new PlaySoundEntry(originalSound, pitch[0], volume[0]));
    }

    private void playSound(String originalSound, float pitchValue, float volumeValue) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        float vx = player == null ? 0 : (float) player.posX;
        float vy = player == null ? 0 : (float) player.posY;
        float vz = player == null ? 0 : (float) player.posZ;

        mc.getSoundHandler().playSound(
          new PositionedSoundRecord(
            new ResourceLocation(originalSound),
            volumeValue,
            pitchValue,
            vx, vy, vz
          )
        );

    }

    private String mapSoundName(String vanillaName) {
        return soundMapper.mapSoundName(vanillaName);
    }

    static class PlaySoundEntry {
        String soundName;
        float pitch;
        float volume;

        PlaySoundEntry(String soundName, float pitch, float volume) {
            this.soundName = soundName;
            this.pitch = pitch;
            this.volume = volume;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PlaySoundEntry)) return false;
            PlaySoundEntry that = (PlaySoundEntry) o;
            return Float.compare(pitch, that.pitch) == 0 && Float.compare(volume, that.volume) == 0 &&
              Objects.equals(soundName, that.soundName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(soundName, pitch, volume);
        }
    }

}