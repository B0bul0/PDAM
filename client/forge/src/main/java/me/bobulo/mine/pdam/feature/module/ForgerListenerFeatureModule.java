package me.bobulo.mine.pdam.feature.module;

import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link FeatureModule} that automatically registers and unregisters Forge event listeners.
 * When the module is enabled, it registers all provided listener objects to the {@link MinecraftForge#EVENT_BUS}.
 * When disabled, it unregisters them.
 */
public final class ForgerListenerFeatureModule extends AbstractFeatureModule {

    public static ForgerListenerFeatureModule of(Object... listeners) {
        return new ForgerListenerFeatureModule(listeners);
    }

    public static ForgerListenerFeatureModule of(List<Object> listeners) {
        return new ForgerListenerFeatureModule(listeners);
    }

    private final List<Object> listeners;

    ForgerListenerFeatureModule(Object... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    ForgerListenerFeatureModule(List<Object> listeners) {
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
