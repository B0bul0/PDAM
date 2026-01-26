package me.bobulo.mine.pdam.feature;

import imgui.flag.ImGuiCond;
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

    private final FeatureService featureService;

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
        for (Feature feature : featureService.getSortedFeatures()) {
            pushID(feature.getId());
            renderFeatureConfig(feature);
            popID();
        }
    }

    private void renderFeatureConfig(Feature feature) {
        if (collapsingHeader(feature.getName())) {
            spacing();
            separatorText("Settings");
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

            spacing();
            spacing();
        }
    }

}
