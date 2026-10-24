package me.bobulo.mine.pdam.feature.behavior;

import me.bobulo.mine.pdam.feature.FeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A {@link FeatureBehavior} implementation that executes callbacks upon being enabled or disabled.
 * This provides a convenient way to define behavior logic using lambda expressions or method references
 * without creating a new class for simple tasks.
 */
public final class CallbackFeatureBehavior extends AbstractFeatureBehavior {

    public static CallbackFeatureBehavior of(Runnable onEnable, Runnable onDisable) {
        return new CallbackFeatureBehavior(context -> onEnable.run(), context -> onDisable.run());
    }

    /**
     * Runs callbacks with phase contexts, e.g. {@code of(context -> start(context.getScope()), context -> stop())}.
     */
    public static CallbackFeatureBehavior of(Consumer<FeatureBehaviorEnableContext> onEnable,
                                             Consumer<FeatureBehaviorDisableContext> onDisable) {
        return new CallbackFeatureBehavior(onEnable, onDisable);
    }

    public static CallbackFeatureBehavior enableOnly(Runnable onEnable) {
        return new CallbackFeatureBehavior(context -> onEnable.run(), null);
    }

    /**
     * Receives the activation context, e.g. {@code enableOnly(context -> context.getScope().registerBinding(binding))}.
     */
    public static CallbackFeatureBehavior enableOnly(Consumer<FeatureBehaviorEnableContext> onEnable) {
        return new CallbackFeatureBehavior(onEnable, null);
    }

    public static CallbackFeatureBehavior disableOnly(Runnable onDisable) {
        return new CallbackFeatureBehavior(null, context -> onDisable.run());
    }

    /**
     * Receives the still available activation scope, e.g. {@code disableOnly(context -> stop(context.getFeature()))}.
     */
    public static CallbackFeatureBehavior disableOnly(Consumer<FeatureBehaviorDisableContext> onDisable) {
        return new CallbackFeatureBehavior(null, onDisable);
    }

    private final Consumer<FeatureBehaviorEnableContext> onEnableCallback;
    private final Consumer<FeatureBehaviorDisableContext> onDisableCallback;

    CallbackFeatureBehavior(Consumer<FeatureBehaviorEnableContext> onEnable,
                            Consumer<FeatureBehaviorDisableContext> onDisable) {
        this.onEnableCallback = onEnable;
        this.onDisableCallback = onDisable;
    }

    @Override
    protected void onEnable(@NotNull FeatureBehaviorEnableContext context) {
        if (this.onEnableCallback != null) {
            this.onEnableCallback.accept(context);
        }
    }

    @Override
    protected void onDisable(@NotNull FeatureBehaviorDisableContext context) {
        if (this.onDisableCallback != null) {
            this.onDisableCallback.accept(context);
        }
    }
}
