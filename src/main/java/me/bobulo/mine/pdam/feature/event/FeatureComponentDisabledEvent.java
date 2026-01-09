package me.bobulo.mine.pdam.feature.event;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.component.FeatureComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event fired when a feature component is disabled.
 */
public class FeatureComponentDisabledEvent extends Event {

    private final Feature feature;
    private final FeatureComponent component;

    public FeatureComponentDisabledEvent(Feature feature, FeatureComponent component) {
        this.feature = feature;
        this.component = component;
    }

    public Feature getFeature() {
        return feature;
    }

    public FeatureComponent getComponent() {
        return component;
    }

}
