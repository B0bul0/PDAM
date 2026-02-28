package me.bobulo.mine.pdam.feature.bungeebypass;

import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import me.bobulo.mine.pdam.feature.imgui.FeatureConfigImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.sign.SignEditor;

import static imgui.ImGui.checkbox;
import static imgui.ImGui.inputText;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class BungeeBypassConfigImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {

    private final ImString spoofedIp = new ImString(BungeeBypass.SPOOFED_IP.get(), 256);
    private final ImString spoofedUuid = new ImString(BungeeBypass.SPOOFED_UUID.get(), 256);
    private final ImString injectedHost = new ImString(BungeeBypass.INJECTED_HOST.get(), 256);

    @Override
    public void draw() {
        if (inputText("Spoofed IP", this.spoofedIp, ImGuiInputTextFlags.CharsNoBlank)) {
            BungeeBypass.SPOOFED_IP.set(this.spoofedIp.get().trim());
        }

        tooltip("The IP address sent to the server to simulate a local connection (usually 127.0.0.1).");

        if (inputText("Spoofed UUID", this.spoofedUuid, ImGuiInputTextFlags.CharsNoBlank)) {
            BungeeBypass.SPOOFED_UUID.set(this.spoofedUuid.get().trim());
        }

        tooltip("The UUID of the player you want to impersonate. Must be a valid UUID format.");

        if (inputText("Injected Host", this.injectedHost, ImGuiInputTextFlags.CharsNoBlank)) {
            BungeeBypass.INJECTED_HOST.set(this.injectedHost.get().trim());
        }

        tooltip("The clean hostname used to bypass the handshake validation (usually \"localhost\").");
    }

}
