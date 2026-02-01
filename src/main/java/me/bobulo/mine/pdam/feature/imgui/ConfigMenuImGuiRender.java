package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.EnabledFeatureModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static imgui.ImGui.*;

/**
 * Renders a configuration menu for a feature using ImGui.
 */
public final class ConfigMenuImGuiRender extends AbstractFeatureModule implements MenuImGuiRender {

    private static final Logger log = LogManager.getLogger(ConfigMenuImGuiRender.class);

    private final boolean includeEnableToggle;
    private List<FeatureConfigImGuiRender> configs;

    public ConfigMenuImGuiRender() {
        this.includeEnableToggle = false;
    }

    public ConfigMenuImGuiRender(boolean includeEnableToggle) {
        this.includeEnableToggle = includeEnableToggle;
    }

    @Override
    protected void onInitialize() {
        this.configs = getFeature().getBehaviors(FeatureConfigImGuiRender.class);
    }

    @Override
    public void draw() {
        if (!includeEnableToggle && (configs == null || configs.isEmpty())) {
            return; // Nothing to render
        }

        if (beginMenu(getFeature().getName())) {

            if (includeEnableToggle) {
                // Enabled toggle
                EnabledFeatureModule enabledFeatureModule = getFeature().getBehavior(EnabledFeatureModule.class);
                if (enabledFeatureModule != null && checkbox("Enabled", getFeature().isEnabled())) {
                    try {
                        enabledFeatureModule.getEnabledConfig().set(!getFeature().isEnabled());
                    } catch (Exception exception) {
                        log.error("Failed to toggle feature {}.", getFeature().getName(), exception);
                    }

                    separator();
                }
            }

            if (includeEnableToggle && !getFeature().isEnabled()) {
                beginDisabled();
            }

            if (configs != null) {
                for (FeatureConfigImGuiRender config : configs) {
                    config.draw();
                }
            }

            if (includeEnableToggle && !getFeature().isEnabled()) {
                endDisabled();
            }

            endMenu();
        }
    }

}
