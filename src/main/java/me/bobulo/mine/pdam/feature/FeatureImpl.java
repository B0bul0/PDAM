package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.attribute.AttributeKey;
import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.feature.event.FeatureDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.scope.LifecycleCleanup;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A feature owns a persistent load scope and a fresh scope for each activation.
 */
public final class FeatureImpl implements Feature {
    private final String id;
    private final Scope loadScope = new Scope(this);
    private Scope enableScope;
    private boolean enabled;
    private boolean transitioning;
    private final List<Consumer<Scope>> enableRegistrations = new ArrayList<>();
    private final Map<AttributeKey<?>, Object> attributes = new ConcurrentHashMap<>();

    /**
     * Creates a disabled feature, e.g. new FeatureImpl("packet_monitor").
     */
    public FeatureImpl(@NotNull String id) {
        Validate.notBlank(id, "Feature id cannot be null or blank");
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return I18n.format("pdam.feature." + id + ".name", id);
    }

    @Override
    public @NotNull String getDescription() {
        return I18n.format("pdam.feature." + id + ".description");
    }

    @Override
    public @NotNull Scope getLoadScope() {
        return loadScope;
    }

    @Override
    public @Nullable Scope getEnableScope() {
        return enableScope;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be enabled from the main thread");
        if (enabled || transitioning) {
            return;
        }
        transitioning = true;
        enabled = true;
        enableScope = new Scope(this);
        try {
            enableScope.activate();
            for (Consumer<Scope> registration : enableRegistrations)
                registration.accept(enableScope);
            loadScope.activate();
        } catch (RuntimeException failure) {
            enabled = false;
            try {
                LifecycleCleanup.run(loadScope::deactivate, enableScope::cleanup);
            } catch (RuntimeException cleanupFailure) {
                if (failure != cleanupFailure) {
                    failure.addSuppressed(cleanupFailure);
                }
            }
            enableScope = null;
            throw failure;
        } finally {
            transitioning = false;
        }
        EventUtils.callEvent(new FeatureEnabledEvent(this));
    }

    @Override
    public void disable() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be disabled from the main thread");
        if (!enabled || transitioning) {
            return;
        }
        transitioning = true;
        enabled = false;
        try {
            LifecycleCleanup.run(loadScope::deactivate, enableScope::cleanup);
        } finally {
            enableScope = null;
            transitioning = false;
        }
        EventUtils.callEvent(new FeatureDisabledEvent(this));
    }

    /* Attributes */

    @Override
    public <T> void set(@NotNull AttributeKey<T> key, T value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(@NotNull AttributeKey<T> key) {
        return (T) attributes.get(key);
    }

    @Override
    public <T> boolean has(@NotNull AttributeKey<T> key) {
        return attributes.containsKey(key);
    }

    @Override
    public <T> void remove(@NotNull AttributeKey<T> key) {
        attributes.remove(key);
    }


    /**
     * Starts a feature definition, e.g. builder().id("example").behavior(behavior).build().
     */
    public static FeatureBuilder builder() {
        return new FeatureBuilder();
    }

    /**
     * Collects typed scope registrations without treating bindings as modules or behaviors.
     */
    public static final class FeatureBuilder {
        private String id;
        private final List<Consumer<FeatureImpl>> registrations = new ArrayList<>();
        private final List<Consumer<Scope>> enableRegistrations = new ArrayList<>();

        private FeatureBuilder() {
        }

        /**
         * Defines the feature identifier.
         */
        public FeatureBuilder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Registers one unique lifetime module.
         */
        public FeatureBuilder module(FeatureModule module) {
            Objects.requireNonNull(module, "Module");
            registrations.add(feature -> feature.addModule(module));
            return this;
        }

        /**
         * Registers unique lifetime modules in order.
         */
        public FeatureBuilder modules(FeatureModule... modules) {
            for (FeatureModule module : modules) module(module);
            return this;
        }

        /**
         * Registers a collection of unique lifetime modules.
         */
        public FeatureBuilder modules(Collection<? extends FeatureModule> modules) {
            for (FeatureModule module : modules) module(module);
            return this;
        }

        /**
         * Registers one repeatable lifetime behavior.
         */
        public FeatureBuilder behavior(FeatureBehavior behavior) {
            Objects.requireNonNull(behavior, "Behavior");
            registrations.add(feature -> feature.addBehavior(behavior));
            return this;
        }

        /**
         * Registers behaviors in order.
         */
        public FeatureBuilder behaviors(FeatureBehavior... behaviors) {
            for (FeatureBehavior behavior : behaviors) behavior(behavior);
            return this;
        }

        /**
         * Defines resources to register in each fresh enable scope.
         */
        public FeatureBuilder onEnable(Consumer<Scope> registration) {
            enableRegistrations.add(Objects.requireNonNull(registration, "Enable registration"));
            return this;
        }

        /**
         * Reinitializes these bindings on every activation, e.g. enableBindings(new ForgeListenerBinding(listener)).
         */
        public FeatureBuilder enableBindings(Binding<?>... bindings) {
            return onEnable(scope -> scope.registerBindings(bindings));
        }

        /**
         * Builds the feature with activation resources available even during automatic enable.
         */
        public FeatureImpl build() {
            FeatureImpl feature = new FeatureImpl(id);
            feature.enableRegistrations.addAll(enableRegistrations);
            try {
                for (Consumer<FeatureImpl> registration : registrations)
                    registration.accept(feature);
                return feature;
            } catch (RuntimeException failure) {
                try {
                    LifecycleCleanup.run(feature::disable, feature.getLoadScope()::cleanup);
                } catch (RuntimeException cleanupFailure) {
                    if (failure != cleanupFailure) {
                        failure.addSuppressed(cleanupFailure);
                    }
                }
                throw failure;
            }
        }
    }
}
