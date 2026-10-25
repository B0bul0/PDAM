package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static imgui.ImGui.*;

/**
 * Renders a configuration menu for a feature using ImGui.
 */
public final class ConfigMenuImGuiRender extends AbstractFeatureBehavior implements MenuImGuiRender {

    private static final Logger log = LogManager.getLogger(ConfigMenuImGuiRender.class);

    private final boolean includeEnableToggle;

    public ConfigMenuImGuiRender() {
        this.includeEnableToggle = false;
    }

    public ConfigMenuImGuiRender(boolean includeEnableToggle) {
        this.includeEnableToggle = includeEnableToggle;
    }

    @Override
    public void draw() {
        List<FeatureConfigImGuiRender> configs = getFeature().getBehaviors(FeatureConfigImGuiRender.class);
        if (!includeEnableToggle && configs.isEmpty()) {
            return; // Nothing to render
        }

        if (beginMenu(getFeature().getName())) {

            if (includeEnableToggle) {
                // Enabled toggle
                if (getFeature().hasBehavior(EnabledFeatureBehavior.class) && checkbox("Enabled", getFeature().isEnabled())) {
                    try {
                        getFeature().toggleEnabled();
                    } catch (Exception exception) {
                        log.error("Failed to toggle feature {}.", getFeature().getName(), exception);
                    }

                    separator();
                }
            }

            if (includeEnableToggle && !getFeature().isEnabled()) {
                beginDisabled();
            }

            for (FeatureConfigImGuiRender config : configs) {
                config.draw();
            }

            if (includeEnableToggle && !getFeature().isEnabled()) {
                endDisabled();
            }

            endMenu();
        }
    }

}
