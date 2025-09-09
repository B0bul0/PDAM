package me.bobulo.mine.devmod.feature;

import me.bobulo.mine.devmod.config.PropertyDeclarer;

public interface Feature extends PropertyDeclarer {

    String getId();

    default String getName() {
        return getId();
    }

    boolean isEnabled();

    void enable();

    void disable();

}
