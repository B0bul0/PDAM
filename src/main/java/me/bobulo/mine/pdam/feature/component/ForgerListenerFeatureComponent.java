package me.bobulo.mine.pdam.feature.component;

import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link FeatureComponent} that automatically registers and unregisters Forge event listeners.
 * When the component is enabled, it registers all provided listener objects to the {@link MinecraftForge#EVENT_BUS}.
 * When disabled, it unregisters them.
 */
public final class ForgerListenerFeatureComponent extends AbstractFeatureComponent {

    public static ForgerListenerFeatureComponent of(Object... listeners) {
        return new ForgerListenerFeatureComponent(listeners);
    }

    public static ForgerListenerFeatureComponent of(List<Object> listeners) {
        return new ForgerListenerFeatureComponent(listeners);
    }

    private final List<Object> listeners;

    ForgerListenerFeatureComponent(Object... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    ForgerListenerFeatureComponent(List<Object> listeners) {
        this.listeners = listeners;
    }

    @Override
    protected void onEnable() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        for (Object listener : this.listeners) {
            if (listener == null) {
                continue;
            }

            MinecraftForge.EVENT_BUS.register(listener);
        }
    }

    @Override
    protected void onDisable() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        for (Object listener : this.listeners) {
            if (listener == null) {
                continue;
            }

            MinecraftForge.EVENT_BUS.unregister(listener);
        }
    }
}
