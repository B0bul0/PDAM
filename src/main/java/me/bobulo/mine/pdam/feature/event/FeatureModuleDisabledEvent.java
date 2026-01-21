package me.bobulo.mine.pdam.feature.event;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event fired when a feature module is disabled.
 */
public class FeatureModuleDisabledEvent extends Event {

    private final Feature feature;
    private final FeatureModule module;

    public FeatureModuleDisabledEvent(Feature feature, FeatureModule module) {
        this.feature = feature;
        this.module = module;
    }

    public Feature getFeature() {
        return feature;
    }

    public FeatureModule getModule() {
        return module;
    }

}
