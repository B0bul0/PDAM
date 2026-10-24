package me.bobulo.mine.pdam.feature.behavior;

import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.FeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorAttachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDetachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation of a FeatureBehavior.
 */
public abstract class AbstractFeatureBehavior implements FeatureBehavior {

    private Feature feature; // Initialized in onAttach
    private boolean enabled = false;

    public Feature getFeature() {
        return feature;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void onAttach(@NotNull FeatureBehaviorAttachContext context) {
        Validate.notNull(context, "Attach context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature == null || this.feature == feature,
          "FeatureBehavior is initialized with a different Feature");

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
    public final void onDetach(@NotNull FeatureBehaviorDetachContext context) {
        Validate.notNull(context, "Detach context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature == feature,
          "FeatureBehavior is initialized with a different Feature");
        Validate.isTrue(!this.enabled,
          "FeatureBehavior must be disabled before detaching from Feature");

        try {
            this.onUninitialize(context);
        } finally {
            this.feature = null;
        }
    }

    @Override
    public final void enable(@NotNull FeatureBehaviorEnableContext context) {
        Validate.notNull(context, "Enable context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature != null,
          "FeatureBehavior not initialized with a Feature");
        Validate.isTrue(this.feature == feature,
          "FeatureBehavior is initialized with a different Feature");

        if (!enabled) {
            enabled = true;
            this.onEnable(context);
        }
    }

    @Override
    public final void disable(@NotNull FeatureBehaviorDisableContext context) {
        Validate.notNull(context, "Disable context cannot be null");
        Feature feature = context.getFeature();
        Validate.isTrue(this.feature != null,
          "FeatureBehavior not initialized with a Feature");
        Validate.isTrue(this.feature == feature,
          "FeatureBehavior is initialized with a different Feature: " +
            this.feature.getId() + " != " + feature.getId());

        if (enabled) {
            enabled = false;
            this.onDisable(context);
        }
    }

    /**
     * Called when the FeatureBehavior is first initialized.
     */
    protected void onInitialize(@NotNull FeatureBehaviorAttachContext context) {
        // Optional
    }

    /**
     * Called after disable and before releasing the feature, e.g. to clean up resources in the context scope.
     * The feature reference is released even if this hook fails.
     */
    protected void onUninitialize(@NotNull FeatureBehaviorDetachContext context) {
        // Optional
    }

    /**
     * Called when the FeatureBehavior is enabled.
     */
    protected void onEnable(@NotNull FeatureBehaviorEnableContext context) {
        // Optional
    }

    /**
     * Called when the FeatureBehavior is disabled.
     */
    protected void onDisable(@NotNull FeatureBehaviorDisableContext context) {
        // Optional
    }

}
