package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.event.FeatureDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureModuleDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureModuleEnabledEvent;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.feature.processor.FeatureProcessor;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Default implementation of a Feature.
 */
public final class FeatureImpl implements Feature, ModularFeature {

    private static final Logger log = LogManager.getLogger(FeatureImpl.class);

    private final String id;
    private final String name;
    private boolean enabled = false;

    private final List<FeatureModule> activeModules = new ArrayList<>();
    private final Map<Class<? extends FeatureBehavior>, FeatureBehavior> registry = new HashMap<>();

    public FeatureImpl(String id) {
        Validate.notBlank(id, "Feature id cannot be null or blank");
        this.id = id;
        this.name = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be enabled from the main thread");

        if (!enabled) {
            log.info("Enabling feature {}", id);

            onEnable();
            enabled = true;

            EventUtils.callEvent(new FeatureEnabledEvent(this));

            log.info("Feature {} enabled", id);
        }
    }

    @Override
    public void disable() {
        Validate.isTrue(ThreadUtils.isMainThread(), "Features can only be disabled from the main thread");

        if (enabled) {
            log.info("Disabling feature {}", id);

            onDisable();
            enabled = false;

            EventUtils.callEvent(new FeatureDisabledEvent(this));

            log.info("Feature {} disabled", id);
        }
    }

    private void onEnable() {
        for (FeatureModule module : activeModules) {
            try {
                module.enable(this);
                EventUtils.callEvent(new FeatureModuleEnabledEvent(this, module));
            } catch (Exception exception) {
                log.error("Error while enabling module {} of feature {}",
                  module.getClass().getSimpleName(), id, exception);
            }
        }
    }

    private void onDisable() {
        for (FeatureModule module : activeModules) {
            try {
                module.disable(this);
                EventUtils.callEvent(new FeatureModuleDisabledEvent(this, module));
            } catch (Exception exception) {
                log.error("Error while disabling module {} of feature {}",
                  module.getClass().getSimpleName(), id, exception);
            }
        }
    }

    /* Register Modules */

    @Override
    public void addModule(@NotNull FeatureModule module) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Modules can only be added from the main thread");
        Validate.isTrue(!activeModules.contains(module),
          "Module instance " + module + " is already registered in feature " + id);

        activeModules.add(module);
        registry.put(module.getClass(), module);

        if (enabled) {
            module.enable(this);
        }
    }

    @Override
    public void removeModule(@NotNull FeatureModule module) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Modules can only be removed from the main thread");

        if (!activeModules.remove(module)) {
            throw new IllegalArgumentException("Module instance " + module + " is not registered in feature " + id);
        }

        if (enabled) {
            module.disable(this);
        }
    }

    @Override
    public @NotNull Collection<FeatureModule> getModules() {
        return Collections.unmodifiableCollection(activeModules);
    }

    /* Processors */

    @Override
    public void addProcessor(@NotNull FeatureProcessor processor) {
        Validate.notNull(processor, "FeatureProcessor cannot be null");
        Validate.isTrue(!registry.containsKey(processor.getClass()),
          "FeatureProcessor of class " + processor.getClass().getSimpleName() + " is already registered");
        registry.put(processor.getClass(), processor);
    }

    @Override
    public void removeProcessor(@NotNull FeatureProcessor processor) {
        Validate.notNull(processor, "FeatureProcessor cannot be null");
        Validate.isTrue(registry.containsKey(processor.getClass()),
          "FeatureProcessor of class " + processor.getClass().getSimpleName() + " is not registered");
        registry.remove(processor.getClass());
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

    /* Builder */

    public static FeatureBuilder builder() {
        return new FeatureBuilder();
    }

    public static final class FeatureBuilder {
        private String id;
        private final List<FeatureModule> modules = new ArrayList<>();
        private final List<FeatureProcessor> processors = new ArrayList<>();

        private FeatureBuilder() {
        }

        public FeatureBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FeatureBuilder modules(Collection<FeatureModule> modules) {
            this.modules.addAll(modules);
            return this;
        }

        public FeatureBuilder modules(FeatureModule... modules) {
            this.modules.addAll(Arrays.asList(modules));
            return this;
        }

        public FeatureBuilder module(FeatureModule module) {
            this.modules.add(module);
            return this;
        }

        public FeatureBuilder processors(Collection<FeatureProcessor> processors) {
            this.processors.addAll(processors);
            return this;
        }

        public FeatureBuilder processors(FeatureProcessor... processors) {
            this.processors.addAll(Arrays.asList(processors));
            return this;
        }

        public FeatureBuilder processor(FeatureProcessor processor) {
            this.processors.add(processor);
            return this;
        }

        public FeatureImpl build() {
            FeatureImpl feature = new FeatureImpl(id);
            this.modules.forEach(feature::addModule);
            this.processors.forEach(feature::addProcessor);
            return feature;
        }
    }
}
