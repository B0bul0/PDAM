package me.bobulo.mine.pdam.feature.signfinder;

import me.bobulo.mine.pdam.util.BlockPosition;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a sign in the Minecraft world with its text and position.
 */
public final class WorldSign {

    private final String[] text; // all 4 lines of the sign
    private final BlockPosition position;

    public WorldSign(String[] text, BlockPosition position) {
        this.text = text;
        this.position = position;
    }

    public String[] getText() {
        return text;
    }

    public BlockPosition getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WorldSign)) return false;
        WorldSign sign = (WorldSign) o;
        return Objects.deepEquals(text, sign.text) && Objects.equals(position, sign.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(text), position);
    }

    @Override
    public String toString() {
        return "Sign{" +
          "text=" + Arrays.toString(text) +
          ", position=" + position +
          '}';
    }
}
