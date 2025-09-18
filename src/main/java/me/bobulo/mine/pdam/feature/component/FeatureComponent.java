package me.bobulo.mine.pdam.feature.component;

import me.bobulo.mine.pdam.config.PropertyDeclarer;
import me.bobulo.mine.pdam.feature.Feature;

public interface FeatureComponent extends PropertyDeclarer {

    void init(Feature feature);

    boolean isEnabled();

    void enable();

    void disable();

}
