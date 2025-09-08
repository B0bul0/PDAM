package me.bobulo.mine.devmod.feature;

public interface Feature {

    String getId();

    default String getName() {
        return getId();
    }

    boolean isEnabled();

    void enable();

    void disable();

}
