package me.bobulo.mine.pdam.feature.sound.mapper;

/**
 * Interface for mapping sound names.
 */
public interface SoundMapper {

    SoundMapper VANILLA = new VanillaSoundMapper();
    SoundMapper BUKKIT_1_8 = new Bukkit_1_8_SoundMapper();

    /**
     * Maps the given sound name to another sound name.
     *
     * @param soundName the original sound name
     * @return the mapped sound name
     */
    String mapSoundName(String soundName);

    /**
     * Reverses the mapping of the given sound name.
     *
     * @param soundName the mapped sound name
     * @return the original sound name
     */
    String reverseMapSoundName(String soundName);
}
