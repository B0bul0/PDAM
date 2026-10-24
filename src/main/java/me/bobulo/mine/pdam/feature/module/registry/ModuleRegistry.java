package me.bobulo.mine.pdam.feature.module.registry;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.event.FeatureModuleDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureModuleEnabledEvent;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleAttachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import me.bobulo.mine.pdam.scope.LifecycleCleanup;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.util.EventUtils;

import java.util.*;

/**
 * Owns unique modules indexed by concrete type for one scope.
 */
public final class ModuleRegistry {

    private final Scope scope;
    private final List<FeatureModule> modules = new ArrayList<>();
    private final Set<FeatureModule> enabled = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Map<Class<? extends FeatureModule>, FeatureModule> byType = new HashMap<>();

    /**
     * Creates a scope registry, e.g. new ModuleRegistry(scope).
     */
    public ModuleRegistry(Scope scope) {
        this.scope = Objects.requireNonNull(scope, "Scope");
    }

    /**
     * Attaches and, when active, enables an instance, e.g. registerModule(module).
     */
    public void registerModule(FeatureModule module) {
        Feature feature = scope.requireFeature();
        Objects.requireNonNull(module, "Module");
        if (byType.containsKey(module.getClass()) || feature.getModule(module.getClass()) != null) {
            throw new IllegalArgumentException("Module already registered: " + module);
        }
        byType.put(module.getClass(), module);
        boolean initialized = false;
        try {
            module.onAttach(new FeatureModuleAttachContext(scope));
            initialized = true;
            modules.add(module);
            if (scope.isActive()) {
                enable(module);
            }
        } catch (RuntimeException failure) {
            if (initialized && byType.get(module.getClass()) == module) {
                try {
                    LifecycleCleanup.run(() -> disable(module),
                      () -> module.onDetach(new FeatureModuleDetachContext(scope)));
                } catch (RuntimeException cleanupFailure) {
                    if (failure != cleanupFailure) {
                        failure.addSuppressed(cleanupFailure);
                    }
                }
            }
            removeIdentity(module);
            enabled.remove(module);
            if (byType.get(module.getClass()) == module) {
                byType.remove(module.getClass());
            }
            throw failure;
        }
    }

    /**
     * Removes one exact instance, e.g. unregisterModule(module).
     */
    public void unregisterModule(FeatureModule module) {
        scope.requireFeature();
        if (module == null || byType.get(module.getClass()) != module || !removeIdentity(module)) {
            throw new IllegalArgumentException("Module not registered: " + module);
        }
        byType.remove(module.getClass());
        LifecycleCleanup.run(() -> disable(module),
          () -> module.onDetach(new FeatureModuleDetachContext(scope)));
    }

    private boolean removeIdentity(FeatureModule module) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i) == module) {
                modules.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Looks up an exact registered type in constant time, e.g. getModule(MyModule.class).
     */
    public <T extends FeatureModule> T getModule(Class<T> type) {
        return type.cast(byType.get(Objects.requireNonNull(type, "Module type")));
    }

    /**
     * Returns the live registration list for inspection; mutate through the registry only.
     */
    public List<FeatureModule> getModules() {
        return modules;
    }

    /**
     * Activates attached instances once, e.g. when the owning scope activates.
     */
    public void enableAll() {
        if (!modules.isEmpty()) {
            scope.requireFeature();
        }
        int index = 0;
        while (index < modules.size()) {
            FeatureModule module = modules.get(index);
            enable(module);
            if (index < modules.size() && modules.get(index) == module) {
                index++;
            }
        }
    }

    private void enable(FeatureModule module) {
        if (enabled.add(module)) {
            module.enable(new FeatureModuleEnableContext(getActivationScope()));
            if (enabled.contains(module)) {
                EventUtils.callEvent(new FeatureModuleEnabledEvent(scope.requireFeature(), module));
            }
        }
    }

    private void disable(FeatureModule module) {
        if (enabled.remove(module)) {
            module.disable(new FeatureModuleDisableContext(getActivationScope()));
            EventUtils.callEvent(new FeatureModuleDisabledEvent(scope.requireFeature(), module));
        }
    }

    private Scope getActivationScope() {
        Scope activation = scope.requireFeature().getEnableScope();
        // Explicit Scope.activate() can also run without enabling the feature itself.
        return activation != null ? activation : scope;
    }

    /**
     * Deactivates every enabled instance, propagating failures after all callbacks run.
     */
    public void disableAll() {
        if (!modules.isEmpty()) {
            scope.requireFeature();
        }
        RuntimeException failure = null;
        int index = 0;
        while (index < modules.size()) {
            FeatureModule module = modules.get(index);
            try {
                disable(module);
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else if (failure != exception) {
                    failure.addSuppressed(exception);
                }
            }
            if (index < modules.size() && modules.get(index) == module) {
                index++;
            }
        }
        if (failure != null) {
            throw failure;
        }
    }

    /**
     * Detaches all registered instances before the owning scope releases bindings.
     */
    public void unregisterAll() {
        if (!modules.isEmpty()) {
            scope.requireFeature();
        }
        RuntimeException failure = null;
        while (!modules.isEmpty()) {
            try {
                unregisterModule(modules.get(0));
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else if (failure != exception) {
                    failure.addSuppressed(exception);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }
}
