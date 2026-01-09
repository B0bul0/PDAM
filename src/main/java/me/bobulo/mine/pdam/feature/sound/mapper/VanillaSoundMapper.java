package me.bobulo.mine.pdam.feature.sound.mapper;

public class VanillaSoundMapper implements SoundMapper {

    @Override
    public String mapSoundName(String soundName) {
        return soundName;
    }

    @Override
    public String reverseMapSoundName(String soundName) {
        return soundName;
    }

}
