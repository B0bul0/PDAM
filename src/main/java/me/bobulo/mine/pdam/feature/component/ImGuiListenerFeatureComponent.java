package me.bobulo.mine.pdam.feature.component;

import me.bobulo.mine.pdam.PDAM;

import java.util.Arrays;
import java.util.List;

public final class ImGuiListenerFeatureComponent extends AbstractFeatureComponent {

    public static ImGuiListenerFeatureComponent of(Object... listeners) {
        return new ImGuiListenerFeatureComponent(listeners);
    }

    public static ImGuiListenerFeatureComponent of(List<Object> windows) {
        return new ImGuiListenerFeatureComponent(windows);
    }

    private final List<Object> listeners;

    ImGuiListenerFeatureComponent(Object... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    ImGuiListenerFeatureComponent(List<Object> listeners) {
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
