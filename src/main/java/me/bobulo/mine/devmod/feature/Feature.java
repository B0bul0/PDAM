package me.bobulo.mine.devmod.feature;

public interface Feature {

    String getName();

    boolean isEnabled();

    void enable();

    void disable();

}
