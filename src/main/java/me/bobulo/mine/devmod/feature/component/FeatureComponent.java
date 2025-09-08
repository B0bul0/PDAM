package me.bobulo.mine.devmod.feature.component;

import me.bobulo.mine.devmod.feature.Feature;

public interface FeatureComponent {

    void init(Feature feature);

    boolean isEnabled();

    void enable();

    void disable();

}
