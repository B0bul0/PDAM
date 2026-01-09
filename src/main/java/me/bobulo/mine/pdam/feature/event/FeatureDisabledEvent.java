package me.bobulo.mine.pdam.feature.event;

import me.bobulo.mine.pdam.feature.Feature;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event fired when a feature is disabled.
 */
public class FeatureDisabledEvent extends Event {

    private final Feature feature;

    public FeatureDisabledEvent(Feature feature) {
        this.feature = feature;
    }

    public Feature getFeature() {
        return feature;
    }

}
