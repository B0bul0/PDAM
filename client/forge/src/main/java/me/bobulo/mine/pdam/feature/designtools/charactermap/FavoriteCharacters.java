package me.bobulo.mine.pdam.feature.designtools.charactermap;

import java.util.*;

/**
 * Used to configure user favorite characters.
 */
public final class FavoriteCharacters {

    private final Set<Character> characters;

    public FavoriteCharacters() {
        this.characters = new HashSet<>();
    }

    public FavoriteCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    public boolean containsCharacter(char character) {
        return characters.contains(character);
    }

    public void addCharacter(char character) {
        characters.add(character);
    }

    public void removeCharacter(char character) {
        characters.remove(character);
    }

    public Collection<Character> getCharacters() {
        return Collections.unmodifiableCollection(characters);
    }

}
