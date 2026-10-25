package me.bobulo.mine.pdam.feature.bungeebypass;

import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;

import static imgui.ImGui.inputText;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class BungeeBypassConfigImGuiRender extends AbstractFeatureBehavior implements FeatureConfigImGuiRender {

    private final ImString spoofedIp = new ImString(BungeeBypassFeature.SPOOFED_IP.get(), 256);
    private final ImString spoofedUuid = new ImString(BungeeBypassFeature.SPOOFED_UUID.get(), 256);
    private final ImString injectedHost = new ImString(BungeeBypassFeature.INJECTED_HOST.get(), 256);

    @Override
    public void draw() {
        if (inputText("Spoofed IP", this.spoofedIp, ImGuiInputTextFlags.CharsNoBlank)) {
            BungeeBypassFeature.SPOOFED_IP.set(this.spoofedIp.get().trim());
        }

        tooltip("The IP address sent to the server to simulate a local connection (usually 127.0.0.1).");

        if (inputText("Spoofed UUID", this.spoofedUuid, ImGuiInputTextFlags.CharsNoBlank)) {
            BungeeBypassFeature.SPOOFED_UUID.set(this.spoofedUuid.get().trim());
        }

        tooltip("The UUID of the player you want to impersonate. Must be a valid UUID format.");

        if (inputText("Injected Host", this.injectedHost, ImGuiInputTextFlags.CharsNoBlank)) {
            BungeeBypassFeature.INJECTED_HOST.set(this.injectedHost.get().trim());
        }

        tooltip("The clean hostname used to bypass the handshake validation (usually \"localhost\").");
    }

}
