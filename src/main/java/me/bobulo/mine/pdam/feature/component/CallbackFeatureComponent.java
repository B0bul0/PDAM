package me.bobulo.mine.pdam.feature.component;

import me.bobulo.mine.pdam.feature.Feature;

import java.util.function.Consumer;

/**
 * A {@link FeatureComponent} implementation that executes callbacks upon being enabled or disabled.
 * This provides a convenient way to define component logic using lambda expressions or method references
 * without creating a new class for simple tasks.
 */
public final class CallbackFeatureComponent extends AbstractFeatureComponent {

    public static CallbackFeatureComponent of(Runnable onEnable, Runnable onDisable) {
        return new CallbackFeatureComponent(feature -> onEnable.run(), feature -> onDisable.run());
    }

    public static CallbackFeatureComponent of(Consumer<Feature> onEnable, Consumer<Feature> onDisable) {
        return new CallbackFeatureComponent(onEnable, onDisable);
    }

    public static CallbackFeatureComponent enableOnly(Runnable onEnable) {
        return new CallbackFeatureComponent(feature -> onEnable.run(), null);
    }

    public static CallbackFeatureComponent enableOnly(Consumer<Feature> onEnable) {
        return new CallbackFeatureComponent(onEnable, null);
    }

    public static CallbackFeatureComponent disableOnly(Runnable onDisable) {
        return new CallbackFeatureComponent(null, feature -> onDisable.run());
    }

    public static CallbackFeatureComponent disableOnly(Consumer<Feature> onDisable) {
        return new CallbackFeatureComponent(null, onDisable);
    }

    private final Consumer<Feature> onEnableCallback;
    private final Consumer<Feature> onDisableCallback;

    CallbackFeatureComponent(Consumer<Feature> onEnable, Consumer<Feature> onDisable) {
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
