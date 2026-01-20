package me.bobulo.mine.pdam.feature.module;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;

import java.util.Arrays;
import java.util.List;

public final class ImGuiListenerFeatureModule extends AbstractFeatureModule {

    public static ImGuiListenerFeatureModule of(ImGuiRenderable... listeners) {
        return new ImGuiListenerFeatureModule(listeners);
    }

    public static ImGuiListenerFeatureModule of(List<ImGuiRenderable> windows) {
        return new ImGuiListenerFeatureModule(windows);
    }

    private final List<ImGuiRenderable> listeners;

    ImGuiListenerFeatureModule(ImGuiRenderable... listeners) {
        this.listeners = ImmutableList.copyOf(listeners);
    }

    ImGuiListenerFeatureModule(List<ImGuiRenderable> listeners) {
        this.listeners = ImmutableList.copyOf(listeners);
    }

    public List<ImGuiRenderable> getListeners() {
        return listeners;
    }

    @Override
    protected void onEnable() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        for (Object window : this.listeners) {
            if (window == null) {
                continue;
            }

            PDAM.getImGuiRenderer().registerWidow(window);
        }
    }

    @Override
    protected void onDisable() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        for (Object window : this.listeners) {
            if (window == null) {
                continue;
            }

            PDAM.getImGuiRenderer().unregisterWidow(window);
        }
    }
}
