package me.bobulo.mine.pdam.feature;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.EnabledFeatureModule;
import me.bobulo.mine.pdam.imgui.util.ImGuiNotificationDrawer;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public final class FeatureConfigWindow extends AbstractRenderItemWindow {

    private static final Logger log = LogManager.getLogger(FeatureConfigWindow.class);

    private final ImGuiNotificationDrawer notificationDrawer = new ImGuiNotificationDrawer();
    private final ImGuiTextFilter searchFeatureFilter = new ImGuiTextFilter();

    private final FeatureService featureService;
    private String selectedFeatureId = null;

    public FeatureConfigWindow(FeatureService featureService) {
        super("Features Configs");
        this.featureService = featureService;
    }

    @Override
    public void renderGui() {
        setNextWindowSize(300, 500, ImGuiCond.FirstUseEver);
        setNextWindowPos(100, 100, ImGuiCond.FirstUseEver);

        if (begin("Features Configs##FeatureConfigWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        if (beginChild("FeaturesList", 180, 0, false, ImGuiWindowFlags.HorizontalScrollbar)) {
            searchFeatureFilter.draw("##SearchFeatures", -1);
            separator();

            for (Feature feature : featureService.getSortedFeatures()) {
                if (searchFeatureFilter.passFilter(filterText(feature)) &&
                  selectable(feature.getName() + "##" + feature.getId(), feature.getId().equals(selectedFeatureId))) {
                    selectedFeatureId = feature.getId();
                }
            }
        }
        endChild();

        sameLine();

        if (beginChild("FeatureSettings", 0, 0, true, ImGuiWindowFlags.HorizontalScrollbar)) {
            if (selectedFeatureId == null || selectedFeatureId.isEmpty()) {
                text("Select a feature to configure.");
            } else {
                Feature selectedFeature = featureService.getFeature(selectedFeatureId);
                if (selectedFeature != null) {
                    renderFeatureConfig(selectedFeature);
                } else {
                    text("Selected feature not found.");
                }
            }
        }

        endChild();
    }

    private void renderFeatureConfig(Feature feature) {
        separatorText("Settings");
        spacing();
        if (!feature.getDescription().isEmpty()) {
            textWrapped(feature.getDescription());
        }
        spacing();

        boolean separatorNeeded = false;

        // Enabled toggle
        EnabledFeatureModule enabledFeatureModule = feature.getBehavior(EnabledFeatureModule.class);
        if (enabledFeatureModule != null) {
            if (checkbox("Enabled", feature.isEnabled())) {
                try {
                    enabledFeatureModule.getEnabledConfig().set(!feature.isEnabled());
                } catch (Exception exception) {
                    log.error("Failed to toggle feature {}.", feature.getName(), exception);
                    notificationDrawer.error("Failed to toggle feature " + feature.getName() + ".");
                }

                separatorNeeded = true;
            }
        }

        if (!feature.isEnabled()) {
            beginDisabled();
        }

        Collection<FeatureConfigImGuiRender> configImGuiRenders = feature.getBehaviors(FeatureConfigImGuiRender.class);
        if (configImGuiRenders != null) {
            for (FeatureConfigImGuiRender configImGuiRender : configImGuiRenders) {
                try {
                    if (separatorNeeded) {
                        separator();
                    }

                    configImGuiRender.draw();
                } catch (Exception exception) {
                    log.error("Failed to render config ImGui for feature {}.", feature.getName(), exception);
                }
            }
        }

        if (!feature.isEnabled()) {
            endDisabled();
        }
    }

    private String filterText(Feature feature) {
        return feature.getName() + " " + feature.getDescription() + " " + feature.getId();
    }

}
