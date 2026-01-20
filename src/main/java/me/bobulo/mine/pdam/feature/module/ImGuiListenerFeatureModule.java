package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.PDAM;

import java.util.Arrays;
import java.util.List;

public final class ImGuiListenerFeatureModule extends AbstractFeatureModule {

    public static ImGuiListenerFeatureModule of(Object... listeners) {
        return new ImGuiListenerFeatureModule(listeners);
    }

    public static ImGuiListenerFeatureModule of(List<Object> windows) {
        return new ImGuiListenerFeatureModule(windows);
    }

    private final List<Object> listeners;

    ImGuiListenerFeatureModule(Object... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    ImGuiListenerFeatureModule(List<Object> listeners) {
        this.listeners = listeners;
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
