package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.feature.event.FeatureModuleDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureModuleEnabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.feature.module.ModularFeature;
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

    private final Map<Class<? extends FeatureModule>, FeatureModule> modules = new HashMap<>();

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
        for (FeatureModule module : modules.values()) {
            try {
                module.enable();
                EventUtils.callEvent(new FeatureModuleEnabledEvent(this, module));
            } catch (Exception exception) {
                log.error("Error while enabling module {} of feature {}",
                  module.getClass().getSimpleName(), id, exception);
            }
        }
    }

    private void onDisable() {
        for (FeatureModule module : modules.values()) {
            try {
                module.disable();
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
        Validate.isTrue(!modules.containsValue(module),
          "Module instance " + module + " is already registered in feature " + id);

        module.init(this);
        modules.put(module.getClass(), module);

        if (enabled) {
            module.enable();
        }
    }

    @Override
    public void removeModule(@NotNull FeatureModule module) {
        Validate.isTrue(ThreadUtils.isMainThread(), "Modules can only be removed from the main thread");

        FeatureModule removed = modules.remove(module.getClass());
        Validate.isTrue(removed != null,
          "Module instance " + module + " is not registered in feature " + id);


        if (enabled) {
            module.disable();
        }
    }

    @Override
    public boolean hasModule(@NotNull FeatureModule module) {
        return modules.get(module.getClass()) == module;
    }

    @Override
    public boolean hasModule(@NotNull Class<? extends FeatureModule> module) {
        return modules.containsKey(module);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FeatureModule> T getModule(@NotNull Class<T> moduleClass) {
        return (T) modules.get(moduleClass);
    }

    @Override
    public @NotNull Collection<FeatureModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    /* Builder */

    public static FeatureBuilder builder() {
        return new FeatureBuilder();
    }

    public static final class FeatureBuilder {
        private String id;
        private final List<FeatureModule> modules = new ArrayList<>();

        private FeatureBuilder() {
        }

        public FeatureBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FeatureBuilder modules(List<FeatureModule> modules) {
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

        public FeatureImpl build() {
            FeatureImpl feature = new FeatureImpl(id);
            this.modules.forEach(feature::addModule);
            return feature;
        }
    }
}
