package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.feature.Feature;

import java.util.function.Consumer;

/**
 * A {@link FeatureModule} implementation that executes callbacks upon being enabled or disabled.
 * This provides a convenient way to define module logic using lambda expressions or method references
 * without creating a new class for simple tasks.
 */
public final class CallbackFeatureModule extends AbstractFeatureModule {

    public static CallbackFeatureModule of(Runnable onEnable, Runnable onDisable) {
        return new CallbackFeatureModule(feature -> onEnable.run(), feature -> onDisable.run());
    }

    public static CallbackFeatureModule of(Consumer<Feature> onEnable, Consumer<Feature> onDisable) {
        return new CallbackFeatureModule(onEnable, onDisable);
    }

    public static CallbackFeatureModule enableOnly(Runnable onEnable) {
        return new CallbackFeatureModule(feature -> onEnable.run(), null);
    }

    public static CallbackFeatureModule enableOnly(Consumer<Feature> onEnable) {
        return new CallbackFeatureModule(onEnable, null);
    }

    public static CallbackFeatureModule disableOnly(Runnable onDisable) {
        return new CallbackFeatureModule(null, feature -> onDisable.run());
    }

    public static CallbackFeatureModule disableOnly(Consumer<Feature> onDisable) {
        return new CallbackFeatureModule(null, onDisable);
    }

    private final Consumer<Feature> onEnableCallback;
    private final Consumer<Feature> onDisableCallback;

    CallbackFeatureModule(Consumer<Feature> onEnable, Consumer<Feature> onDisable) {
        this.onEnableCallback = onEnable;
        this.onDisableCallback = onDisable;
    }

    @Override
    protected void onEnable() {
        if (this.onEnableCallback != null) {
            this.onEnableCallback.accept(this.getFeature());
        }
    }

    @Override
    protected void onDisable() {
        if (this.onDisableCallback != null) {
            this.onDisableCallback.accept(this.getFeature());
        }
    }
}
