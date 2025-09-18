package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.config.PropertyDeclarer;

public interface Feature extends PropertyDeclarer {

    String getId();

    default String getName() {
        return getId();
    }

    boolean isEnabled();

    void enable();

    void disable();

}
