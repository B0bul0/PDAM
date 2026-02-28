package me.bobulo.mine.pdam.feature.event;

import me.bobulo.mine.pdam.feature.Feature;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event fired when a feature is enabled.
 */
public class FeatureEnabledEvent extends Event {

    private final Feature feature;

    public FeatureEnabledEvent(Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

}
