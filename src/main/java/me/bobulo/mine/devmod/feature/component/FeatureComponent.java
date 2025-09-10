package me.bobulo.mine.devmod.feature.component;

import me.bobulo.mine.devmod.config.PropertyDeclarer;
import me.bobulo.mine.devmod.feature.Feature;

public interface FeatureComponent extends PropertyDeclarer {

    void init(Feature feature);

    boolean isEnabled();

    void enable();

    void disable();

}
