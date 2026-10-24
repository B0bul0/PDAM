package me.bobulo.mine.pdam.scope;

import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.binding.context.BindingContext;
import me.bobulo.mine.pdam.binding.registry.BindingRegistry;
import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.FeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.registry.BehaviorRegistry;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.feature.module.registry.ModuleRegistry;
import me.bobulo.mine.pdam.util.ThreadUtils;

import java.util.List;

/**
 * Owns module, behavior and binding registrations for one lifecycle.
 */
public final class Scope {

    private final Feature feature;
    private final ModuleRegistry moduleRegistry = new ModuleRegistry(this);
    private final BehaviorRegistry behaviorRegistry = new BehaviorRegistry(this);
    private final BindingRegistry bindingRegistry = new BindingRegistry(new BindingContext(this));
    private boolean active;

    /**
     * Creates an independent resource scope, e.g. new Scope().
     */
    public Scope() {
        this(null);
    }

    /**
     * Creates a feature scope, e.g. new Scope(feature).
     */
    public Scope(Feature feature) {
        this.feature = feature;
    }

    /**
     * Returns the module registry, e.g. scope.getModuleRegistry().get(MyModule.class).
     */
    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    /**
     * Returns the behavior registry, e.g. scope.getBehaviorRegistry().get(MyBehavior.class).
     */
    public BehaviorRegistry getBehaviorRegistry() {
        return behaviorRegistry;
    }

    /**
     * Returns the binding registry, e.g. scope.getBindingRegistry().gets(Listener.class).
     */
    public BindingRegistry getBindingRegistry() {
        return bindingRegistry;
    }

    /**
     * Registers a unique module, e.g. scope.registerModule(module).
     */
    public Scope registerModule(FeatureModule module) {
        moduleRegistry.registerModule(module);
        return this;
    }

    /**
     * Removes a module and releases its callbacks.
     */
    public Scope unregisterModule(FeatureModule module) {
        moduleRegistry.unregisterModule(module);
        return this;
    }

    /**
     * Registers a repeatable behavior, e.g. scope.registerBehavior(behavior).
     */
    public Scope registerBehavior(FeatureBehavior behavior) {
        behaviorRegistry.registerBehavior(behavior);
        return this;
    }

    /**
     * Removes a behavior and releases its callbacks.
     */
    public Scope unregisterBehavior(FeatureBehavior behavior) {
        behaviorRegistry.unregisterBehavior(behavior);
        return this;
    }

    /**
     * Initializes a binding immediately, e.g. scope.registerBinding(binding).
     */
    public Scope registerBinding(Binding<?> binding) {
        bindingRegistry.registerBinding(binding);
        return this;
    }

    /**
     * Initializes several bindings, e.g. scope.registerBindings(first, second).
     */
    public Scope registerBindings(Binding<?>... bindings) {
        bindingRegistry.registerBindings(bindings);
        return this;
    }

    /**
     * Removes and closes a binding, e.g. scope.unregisterBinding(binding).
     */
    public Scope unregisterBinding(Binding<?> binding) {
        bindingRegistry.unregisterBinding(binding);
        return this;
    }

    /**
     * Returns bindings for inspection; registration methods own mutation.
     */
    public List<Binding<?>> getBindings() {
        return bindingRegistry.getBindings();
    }

    /**
     * Looks up a unique module, e.g. scope.getModule(MyModule.class).
     */
    public <T extends FeatureModule> T getModule(Class<T> type) {
        return moduleRegistry.getModule(type);
    }

    /**
     * Looks up every behavior of a type, e.g. scope.getBehavior(MyBehavior.class).
     */
    public <T extends FeatureBehavior> List<T> getBehavior(Class<T> type) {
        return behaviorRegistry.get(type);
    }

    /**
     * Looks up the first bound value of a type, e.g. scope.get(Listener.class).
     */
    public <T> T get(Class<T> type) {
        return bindingRegistry.get(type);
    }

    /**
     * Looks up all assignable bound values, e.g. scope.gets(Listener.class).
     */
    public <T> List<T> gets(Class<T> type) {
        return bindingRegistry.gets(type);
    }

    /**
     * Internal feature lifecycle context; also enforces game-thread mutations.
     */
    public Feature requireFeature() {
        if (feature == null) {
            throw new IllegalStateException("Module and behavior callbacks require a feature scope");
        }
        if (!ThreadUtils.isMainThread()) {
            throw new IllegalArgumentException("Feature scopes require the main thread");
        }
        return feature;
    }

    /**
     * Whether this scope currently activates its registered components.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activates components once; registrations made during callbacks activate immediately.
     */
    public void activate() {
        if (feature != null) {
            requireFeature();
        }
        if (active) {
            return;
        }
        active = true;
        moduleRegistry.enableAll();
        behaviorRegistry.enableAll();
    }

    /**
     * Deactivates components while keeping their registrations and bindings.
     */
    public void deactivate() {
        if (feature != null) {
            requireFeature();
        }
        if (!active) {
            return;
        }
        active = false;
        LifecycleCleanup.run(moduleRegistry::disableAll, behaviorRegistry::disableAll);
    }

    /**
     * Releases components before bindings, attempting every cleanup even after a failure.
     */
    public void cleanup() {
        if (feature != null) {
            requireFeature();
        }
        LifecycleCleanup.run(this::deactivate, moduleRegistry::unregisterAll,
          behaviorRegistry::unregisterAll, bindingRegistry::unregisterAll);
    }
}
