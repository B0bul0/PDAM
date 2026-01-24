package me.bobulo.mine.pdam.feature;

import imgui.flag.ImGuiCond;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.imgui.util.ImGuiNotificationDrawer;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        setNextWindowSize(300, 500);
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

          if (checkbox("Enabled", feature.isEnabled())) {
              try {
                  ConfigProperty<Boolean> enabled = ConfigProperty.of(feature.getId() + ".enabled", Boolean.class);
                  if (feature.isEnabled()) {
                      feature.disable();
                      enabled.set(false);
                  } else {
                      feature.enable();
                      enabled.set(true);
                  }
              } catch (Exception exception) {
                  log.error("Failed to toggle feature {}.", feature.getName(), exception);
                  notificationDrawer.error("Failed to toggle feature " + feature.getName() + ".");
              }
          }

          if (!feature.isEnabled()) {
              beginDisabled();
          }

          FeatureConfigImGuiRender configImGuiRender = feature.getBehavior(FeatureConfigImGuiRender.class);
          if (configImGuiRender != null) {
              configImGuiRender.draw();
          }

          if (!feature.isEnabled()) {
              endDisabled();
          }

          spacing();
          spacing();
      }
    }

}
