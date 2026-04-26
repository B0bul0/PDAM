package me.bobulo.mine.pdam.feature.update;

import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.disableIf;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class UpdateConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

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

        if (checkbox("Enable Chat Warning", Update.CHAT_WARNING.get())) {
            Update.CHAT_WARNING.set(!Update.CHAT_WARNING.get());
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
            String url = String.format(Update.GITHUB_URL_DOWNLOAD, Update.GITHUB_USER, Update.GITHUB_REPO);
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
