package me.bobulo.mine.pdam.feature.module;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleAttachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation of a FeatureModule.
 */
public abstract class AbstractFeatureModule implements FeatureModule {

    private Feature feature; // Initialized in onAttach
    private boolean enabled = false;

    public Feature getFeature() {
        return feature;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void onAttach(@NotNull FeatureModuleAttachContext context) {
        Validate.notNull(context, "Attach context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature == null || this.feature == feature,
          "FeatureModule is initialized with a different Feature");

        if (this.feature == null) {
            this.feature = feature;
            try {
                this.onInitialize(context);
            } catch (RuntimeException failure) {
                this.feature = null;
                throw failure;
            }
        }
    }

    @Override
    public final void onDetach(@NotNull FeatureModuleDetachContext context) {
        Validate.notNull(context, "Detach context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature");
        Validate.isTrue(!this.enabled,
          "FeatureModule must be disabled before detaching from Feature");

        try {
            this.onUninitialize(context);
        } finally {
            this.feature = null;
        }
    }

    @Override
    public final void enable(@NotNull FeatureModuleEnableContext context) {
        Validate.notNull(context, "Enable context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature != null,
          "FeatureModule not initialized with a Feature");
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature");

        if (!enabled) {
            enabled = true;
            this.onEnable(context);
        }
    }

    @Override
    public final void disable(@NotNull FeatureModuleDisableContext context) {
        Validate.notNull(context, "Disable context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature != null,
          "FeatureModule not initialized with a Feature");
        Validate.isTrue(this.feature == feature,
          "FeatureModule is initialized with a different Feature: " +
            this.feature.getId() + " != " + feature.getId());

        if (enabled) {
            enabled = false;
            this.onDisable(context);
        }
    }

    /**
     * Called when the FeatureModule is first initialized.
     */
    protected void onInitialize(@NotNull FeatureModuleAttachContext context) {
        // Optional
    }

    /**
     * Called after disable and before releasing the feature, e.g. to unregister resources from the context scope.
     * The feature reference is released even if this hook fails.
     */
    protected void onUninitialize(@NotNull FeatureModuleDetachContext context) {
        // Optional
    }

    /**
     * Called when the FeatureModule is enabled.
     */
    protected void onEnable(@NotNull FeatureModuleEnableContext context) {
        // Optional
    }

    /**
     * Called when the FeatureModule is disabled.
     */
    protected void onDisable(@NotNull FeatureModuleDisableContext context) {
        // Optional
    }

}
