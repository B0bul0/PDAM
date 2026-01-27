package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ModuleManager implements ModularFeature {

    private final Feature feature;

    private final List<FeatureModule> activeModules = new CopyOnWriteArrayList<>();
    private final Map<Class<? extends FeatureBehavior>, FeatureBehavior> registry = new ConcurrentHashMap<>();

    public ModuleManager(@NotNull Feature feature) {
        Validate.notNull(feature, "Feature cannot be null");
        this.feature = feature;
    }

    /* Register Modules */

    @Override
    public void addModule(@NotNull FeatureModule module) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Modules can only be added from the main thread");
        Validate.isTrue(!activeModules.contains(module),
          "Module instance " + module + " is already registered in feature " + feature.getId());

        activeModules.add(module);
        registry.put(module.getClass(), module);

        module.onAttach(feature);

        if (feature.isEnabled()) {
            module.enable(feature);
        }
    }

    @Override
    public void removeModule(@NotNull FeatureModule module) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Modules can only be removed from the main thread");
        FeatureBehavior featureBehavior = registry.get(module.getClass());
        Validate.isTrue(featureBehavior == module,
          "Module instance " + module + " is not registered in feature " + feature.getId());

        registry.remove(module.getClass());
        activeModules.remove(module);

        if (feature.isEnabled()) {
            module.disable(feature);
        }

        module.onDetach(feature);
    }

    @Override
    public @NotNull Collection<FeatureModule> getModules() {
        return Collections.unmodifiableCollection(activeModules);
    }

    /* Behaviors */

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FeatureBehavior> T getBehavior(@NotNull Class<T> behaviorClass) {
        return (T) registry.get(behaviorClass);
    }

    @Override
    public <T extends FeatureBehavior> boolean hasBehavior(@NotNull Class<T> behaviorClass) {
        return registry.containsKey(behaviorClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FeatureBehavior> Collection<T> getBehaviors() {
        return (Collection<T>) Collections.unmodifiableCollection(registry.values());
    }

    @Override
    public <T extends FeatureBehavior> List<T> getBehaviors(@NotNull Class<T> behaviorClass) {
        List<T> behaviors = new ArrayList<>();
        for (FeatureBehavior behavior : registry.values()) {
            if (behaviorClass.isInstance(behavior)) {
                behaviors.add(behaviorClass.cast(behavior));
            }
        }
        return behaviors;
    }

}
