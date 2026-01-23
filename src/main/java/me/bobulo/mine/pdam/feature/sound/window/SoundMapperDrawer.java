package me.bobulo.mine.pdam.feature.sound.window;

import me.bobulo.mine.pdam.feature.sound.mapper.SoundMapper;

import static imgui.ImGui.*;

public class SoundMapperDrawer {

    private SoundMapper soundMapper = SoundMapper.VANILLA;

    public void draw() {
        text("Sound Name Mapping:");

        if (radioButton("Vanilla", soundMapper == SoundMapper.VANILLA)) {
            soundMapper = SoundMapper.VANILLA;
        }

        sameLine();

        if (radioButton("Bukkit 1.8", soundMapper == SoundMapper.BUKKIT_1_8)) {
            soundMapper = SoundMapper.BUKKIT_1_8;
        }

    }

    public String mapSoundName(String vanillaName) {
        if (soundMapper == null) {
            return vanillaName;
        }

        String mappedName = soundMapper.mapSoundName(vanillaName);
        return mappedName != null ? mappedName : vanillaName;
    }

    public String reverseMapSoundName(String mappedName) {
        if (soundMapper == null) {
            return mappedName;
        }

        String vanillaName = soundMapper.reverseMapSoundName(mappedName);
        return vanillaName != null ? vanillaName : mappedName;
    }

    public SoundMapper getSoundMapper() {
        return soundMapper;
    }

}
