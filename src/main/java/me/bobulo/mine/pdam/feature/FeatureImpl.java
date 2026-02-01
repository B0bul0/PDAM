package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.event.FeatureDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureModuleDisabledEvent;
import me.bobulo.mine.pdam.feature.event.FeatureModuleEnabledEvent;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.attribute.AttributeKey;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of a Feature.
 */
public final class FeatureImpl implements Feature, ModularFeature {

    private static final Logger log = LogManager.getLogger(FeatureImpl.class);

    private final String id;
    private final String name;
    private boolean enabled = false;

    private final ModuleManager moduleManager;
    private final Map<AttributeKey<?>, Object> attributes = new ConcurrentHashMap<>();

    public FeatureImpl(@NotNull String id) {
        Validate.notBlank(id, "Feature id cannot be null or blank");
        this.id = id;
        this.name = id;
        this.moduleManager = new ModuleManager(this);
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return I18n.format("pdam.feature." + id + ".name", name);
    }

    @Override
    public @NotNull String getDescription() {
        return I18n.format("pdam.feature." + id + ".description");
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
        for (FeatureModule module : moduleManager.getModules()) {
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
        for (FeatureModule module : moduleManager.getModules()) {
            try {
                module.disable(this);
                EventUtils.callEvent(new FeatureModuleDisabledEvent(this, module));
            } catch (Exception exception) {
                log.error("Error while disabling module {} of feature {}",
                  module.getClass().getSimpleName(), id, exception);
            }
        }
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


    /* Register Modules */

    @Override
    public void addModule(@NotNull FeatureModule module) {
        moduleManager.addModule(module);
    }

    @Override
    public void removeModule(@NotNull FeatureModule module) {
        moduleManager.removeModule(module);
    }

    @Override
    public @NotNull Collection<FeatureModule> getModules() {
        return moduleManager.getModules();
    }

    /* Behaviors */

    @Override
    public <T extends FeatureBehavior> T getBehavior(@NotNull Class<T> behaviorClass) {
        return moduleManager.getBehavior(behaviorClass);
    }

    @Override
    public <T extends FeatureBehavior> boolean hasBehavior(@NotNull Class<T> behaviorClass) {
        return moduleManager.hasBehavior(behaviorClass);
    }

    @Override
    public <T extends FeatureBehavior> Collection<T> getBehaviors() {
        return moduleManager.getBehaviors();
    }

    @Override
    public <T extends FeatureBehavior> List<T> getBehaviors(@NotNull Class<T> behaviorClass) {
        return moduleManager.getBehaviors(behaviorClass);
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

        public FeatureImpl build() {
            FeatureImpl feature = new FeatureImpl(id);
            this.modules.forEach(feature::addModule);
            return feature;
        }
    }
}
