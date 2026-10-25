package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.context.FeatureUninitializeContext;
import me.bobulo.mine.pdam.feature.event.FeatureDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.scope.LifecycleCleanup;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.apache.commons.lang3.Validate;

/**
 * Owns a persistent load scope and a fresh scope for each activation on the game thread.
 */
final class FeatureController {
    private final AbstractFeature feature;
    private final Scope loadScope;
    private Scope enableScope;
    private Phase phase = Phase.NEW;

    FeatureController(AbstractFeature feature) {
        this.feature = feature;
        this.loadScope = new Scope(feature);
    }

    Scope getLoadScope() {
        return loadScope;
    }

    Scope getEnableScope() {
        return enableScope;
    }

    boolean isEnabled() {
        return phase == Phase.ENABLING || phase == Phase.ENABLED;
    }

    void initialize() {
        requireMainThread();
        if (phase != Phase.NEW) {
            return;
        }
        phase = Phase.INITIALIZING;
        try {
            feature.onInitialize(new FeatureInitializeContext(loadScope));
            boolean enableRequested = phase == Phase.ENABLE_REQUESTED;
            phase = Phase.DISABLED;
            if (enableRequested) {
                enable();
            }
        } catch (RuntimeException failure) {
            if (phase == Phase.INITIALIZING || phase == Phase.ENABLE_REQUESTED) {
                phase = Phase.DISABLED;
            }
            rollback(failure, this::uninitialize);
            throw failure;
        }
    }

    void enable() {
        requireMainThread();
        if (phase == Phase.NEW) {
            initialize();
        }
        if (phase == Phase.INITIALIZING || phase == Phase.ENABLE_REQUESTED) {
            // Configuration behaviors may request enable while the feature is still initializing.
            phase = Phase.ENABLE_REQUESTED;
            return;
        }
        if (phase != Phase.DISABLED) {
            return;
        }
        phase = Phase.ENABLING;
        enableScope = new Scope(feature);
        try {
            enableScope.activate();
            feature.onEnable(new FeatureEnableContext(enableScope));
            loadScope.activate();
        } catch (RuntimeException failure) {
            phase = Phase.DISABLING;
            rollback(failure, () -> feature.onDisable(new FeatureDisableContext(enableScope)),
                    loadScope::deactivate, enableScope::cleanup);
            enableScope = null;
            phase = Phase.DISABLED;
            throw failure;
        }
        phase = Phase.ENABLED;
        EventUtils.callEvent(new FeatureEnabledEvent(feature));
    }

    void disable() {
        requireMainThread();
        if (phase == Phase.ENABLE_REQUESTED) {
            phase = Phase.INITIALIZING;
            return;
        }
        if (phase != Phase.ENABLED) {
            return;
        }
        phase = Phase.DISABLING;
        try {
            LifecycleCleanup.run(() -> feature.onDisable(new FeatureDisableContext(enableScope)),
                    loadScope::deactivate, enableScope::cleanup);
        } finally {
            enableScope = null;
            phase = Phase.DISABLED;
        }
        EventUtils.callEvent(new FeatureDisabledEvent(feature));
    }

    void uninitialize() {
        requireMainThread();
        if (phase == Phase.NEW) {
            return;
        }
        Validate.isTrue(phase == Phase.DISABLED || phase == Phase.ENABLED,
                "Feature cannot be removed during a lifecycle transition: " + feature.getId());
        try {
            LifecycleCleanup.run(this::disable, () -> {
                phase = Phase.UNINITIALIZING;
                feature.onUninitialize(new FeatureUninitializeContext(loadScope));
            }, loadScope::cleanup);
        } finally {
            phase = Phase.NEW;
        }
    }

    private static void rollback(RuntimeException failure, Runnable... actions) {
        try {
            LifecycleCleanup.run(actions);
        } catch (RuntimeException cleanupFailure) {
            if (failure != cleanupFailure) {
                failure.addSuppressed(cleanupFailure);
            }
        }
    }

    private static void requireMainThread() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Feature lifecycle requires the main thread");
    }

    private enum Phase {
        NEW, INITIALIZING, ENABLE_REQUESTED, DISABLED, ENABLING, ENABLED, DISABLING, UNINITIALIZING
    }
}
