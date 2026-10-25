package me.bobulo.mine.pdam.feature.behavior.registry;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.FeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorAttachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDetachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import me.bobulo.mine.pdam.scope.LifecycleCleanup;
import me.bobulo.mine.pdam.scope.Scope;

import java.util.*;

/**
 * Owns repeatable behaviors in registration order for one scope.
 */
public final class BehaviorRegistry {

    private final Scope scope;
    private final List<FeatureBehavior> behaviors = new ArrayList<>();
    private final Set<FeatureBehavior> attached = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<FeatureBehavior> enabled = Collections.newSetFromMap(new IdentityHashMap<>());

    /**
     * Creates a scope registry, e.g. new BehaviorRegistry(scope).
     */
    public BehaviorRegistry(Scope scope) {
        this.scope = Objects.requireNonNull(scope, "Scope");
    }

    /**
     * Attaches and, when active, enables an instance, e.g. registerBehavior(behavior).
     */
    public void registerBehavior(FeatureBehavior behavior) {
        Feature feature = scope.requireFeature();
        Objects.requireNonNull(behavior, "Behavior");
        if (contains(behavior) || feature.getLoadScope().getBehaviorRegistry().contains(behavior)
          || (feature.getEnableScope() != null && feature.getEnableScope().getBehaviorRegistry().contains(behavior))) {
            throw new IllegalArgumentException("Behavior already registered: " + behavior);
        }
        attached.add(behavior);
        boolean initialized = false;
        try {
            behavior.onAttach(new FeatureBehaviorAttachContext(scope));
            initialized = true;
            behaviors.add(behavior);
            if (scope.isActive()) {
                enable(behavior);
            }
        } catch (RuntimeException failure) {
            if (initialized && attached.contains(behavior)) {
                try {
                    LifecycleCleanup.run(() -> disable(behavior),
                      () -> behavior.onDetach(new FeatureBehaviorDetachContext(scope)));
                } catch (RuntimeException cleanupFailure) {
                    if (failure != cleanupFailure) {
                        failure.addSuppressed(cleanupFailure);
                    }
                }
            }
            removeIdentity(behavior);
            attached.remove(behavior);
            enabled.remove(behavior);

            throw failure;
        }
    }

    /**
     * Removes one exact instance, e.g. unregisterBehavior(behavior).
     */
    public void unregisterBehavior(FeatureBehavior behavior) {
        scope.requireFeature();
        if (!attached.contains(behavior) || !removeIdentity(behavior)) {
            throw new IllegalArgumentException("Behavior not registered: " + behavior);
        }

        attached.remove(behavior);
        LifecycleCleanup.run(() -> disable(behavior),
          () -> behavior.onDetach(new FeatureBehaviorDetachContext(scope)));
    }

    /**
     * Tests registration by identity, including an attachment currently in progress.
     */
    public boolean contains(FeatureBehavior behavior) {
        return attached.contains(behavior);
    }

    private boolean removeIdentity(FeatureBehavior behavior) {
        for (int i = 0; i < behaviors.size(); i++) {
            if (behaviors.get(i) == behavior) {
                behaviors.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns every assignable behavior in registration order, e.g. getBehaviors(MyBehavior.class).
     */
    public <T extends FeatureBehavior> List<T> getBehaviors(Class<T> type) {
        List<T> result = new ArrayList<>();
        collectBehaviors(type, result);
        return result;
    }

    /**
     * Appends matching instances without intermediate lists, e.g. collectBehaviors(Menu.class, result).
     */
    public <T extends FeatureBehavior> void collectBehaviors(Class<T> type, Collection<? super T> result) {
        Objects.requireNonNull(type, "Behavior type");
        Objects.requireNonNull(result, "Behavior results");
        for (FeatureBehavior behavior : behaviors) {
            if (type.isInstance(behavior)) {
                result.add(type.cast(behavior));
            }
        }
    }

    /**
     * Returns the live registration list for inspection; mutate through the registry only.
     */
    public List<FeatureBehavior> getBehaviors() {
        return behaviors;
    }

    /**
     * Activates attached instances once, e.g. when the owning scope activates.
     */
    public void enableAll() {
        if (!behaviors.isEmpty()) {
            scope.requireFeature();
        }
        int index = 0;
        while (index < behaviors.size()) {
            FeatureBehavior behavior = behaviors.get(index);
            enable(behavior);
            if (index < behaviors.size() && behaviors.get(index) == behavior) {
                index++;
            }
        }
    }

    private void enable(FeatureBehavior behavior) {
        if (enabled.add(behavior)) {
            behavior.enable(new FeatureBehaviorEnableContext(getActivationScope()));
        }
    }

    private void disable(FeatureBehavior behavior) {
        if (enabled.remove(behavior)) {
            behavior.disable(new FeatureBehaviorDisableContext(getActivationScope()));
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
        if (!behaviors.isEmpty()) {
            scope.requireFeature();
        }
        RuntimeException failure = null;
        int index = 0;
        while (index < behaviors.size()) {
            FeatureBehavior behavior = behaviors.get(index);
            try {
                disable(behavior);
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else if (failure != exception) {
                    failure.addSuppressed(exception);
                }
            }
            if (index < behaviors.size() && behaviors.get(index) == behavior) {
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
        if (!behaviors.isEmpty()) {
            scope.requireFeature();
        }
        RuntimeException failure = null;
        while (!behaviors.isEmpty()) {
            try {
                unregisterBehavior(behaviors.get(0));
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
