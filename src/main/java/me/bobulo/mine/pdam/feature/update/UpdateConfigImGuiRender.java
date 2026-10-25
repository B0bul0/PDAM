package me.bobulo.mine.pdam.feature.update;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.net.URI;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.disableIf;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class UpdateConfigImGuiRender extends AbstractFeatureBehavior implements FeatureConfigImGuiRender {

    private static final Logger log = LogManager.getLogger(UpdateConfigImGuiRender.class);
    private final UpdateChecker updateChecker;

    private boolean checkingForUpdates = false;

    public UpdateConfigImGuiRender(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @Override
    public void draw() {
        if (updateChecker == null) {
            text("Update checker is not initialized.");
            return;
        }

        if (checkbox("Enable Chat Warning", UpdateFeature.CHAT_WARNING.get())) {
            UpdateFeature.CHAT_WARNING.set(!UpdateFeature.CHAT_WARNING.get());
        }
        tooltip("Show a warning in chat when an update is available.");

        separatorText("Update Status");

        String current = updateChecker.getCurrentVersion();
        String latest = updateChecker.getLatestVersion();

        text("Current Version: " + current);
        text("Latest Version: " + (latest != null ? latest : "Unknown"));

        if (button(checkingForUpdates ? "Checking for Updates..." : "Check for Updates")) {
            if (!checkingForUpdates) {
                checkingForUpdates = true;
                updateChecker.asyncCheckForUpdates()
                  .whenComplete((result, ex) -> {
                      checkingForUpdates = false;
                      if (ex != null) {
                          log.error("Failed to check for updates", ex);
                      }
                  });
            } else {
                log.debug("Already checking for updates, please wait...");
            }
        }

        sameLine();

        disableIf(isBrowserSupported(), () -> {
            String url = String.format(UpdateFeature.GITHUB_URL_DOWNLOAD, UpdateFeature.GITHUB_USER, UpdateFeature.GITHUB_REPO);
            if (button("Open Download Page")) {
                openUrl(url);
            }
        });
    }

    private boolean isBrowserSupported() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            log.error("Failed to open URL: {}", url, e);
        }
    }
}
