package me.bobulo.mine.pdam.feature.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class UpdateChecker {

    private static final Logger log = LogManager.getLogger(UpdateChecker.class);

    private static final String GITHUB_API_URL = "https://api.github.com/repos/%s/%s/releases/latest";

    private final String currentVersion;

    private boolean updateAvailable = false;
    private String latestVersion;

    public UpdateChecker(String currentVersion) {
        this.currentVersion = ensureVPrefix(currentVersion);
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public void asyncCheckForUpdates() {
        new Thread(this::checkForUpdates, "UpdateCheckerThread").start();
    }

    public void checkForUpdates() {
        try {
            log.debug("Checking for updates...");

            String urlString = String.format(GITHUB_API_URL, Update.GITHUB_USER, Update.GITHUB_REPO);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent", "Minecraft-PDAM");

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

                String tagName = json.get("tag_name").getAsString(); // version is in the "tag_name" field
                this.latestVersion = tagName;

                if (isNewerVersion(tagName)) {
                    this.updateAvailable = true;
                    log.info("A new version of the mod is available: {}", tagName);
                } else if (currentVersion.equalsIgnoreCase(tagName)) {
                    log.info("Running the latest version of the mod ({}).", currentVersion);
                } else {
                    log.info("Running a newer version ({}) than the latest release ({}).", currentVersion, tagName);
                }

                reader.close();
            } else {
                log.error("Failed to check for updates: HTTP {}", conn.getResponseCode());
            }

            conn.disconnect();
        } catch (Exception e) {
            log.error("Error checking for updates", e);
        }
    }

    private String ensureVPrefix(String version) {
        return version.startsWith("v") || version.startsWith("V") ? version : "v" + version;
    }

    /**
     * Compares the current version with the GitHub version to determine if the GitHub version is newer.
     */
    private boolean isNewerVersion(String github) {
        String[] vCurrent = currentVersion.replaceAll("[^0-9.]", "").split("\\.");
        String[] vGithub = github.replaceAll("[^0-9.]", "").split("\\.");

        int length = Math.max(vCurrent.length, vGithub.length);

        for (int i = 0; i < length; i++) {
            int numCurrent = i < vCurrent.length ? Integer.parseInt(vCurrent[i]) : 0;
            int numGithub = i < vGithub.length ? Integer.parseInt(vGithub[i]) : 0;

            if (numGithub > numCurrent) return true;
            if (numGithub < numCurrent) return false;
        }

        return false;
    }

}