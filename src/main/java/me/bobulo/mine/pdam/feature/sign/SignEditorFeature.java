package me.bobulo.mine.pdam.feature.sign;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import org.jetbrains.annotations.NotNull;

/**
 * Feature that provides enhanced sign editing capabilities.
 */
public final class SignEditorFeature extends AbstractFeature {

    public static final String FEATURE_ID = "sign_editor";

    /**
     * If true, overrides the default sign GUI with a custom one.
     */
    public static final ConfigProperty<Boolean> OVERRIDE_SIGN_GUI = ConfigProperty.of(FEATURE_ID + ".override_gui", true);

    /**
     * If true, enables the sign viewer GUI when right-clicking a sign while sneaking.
     */
    public static final ConfigProperty<Boolean> VIEW_GUI = ConfigProperty.of(FEATURE_ID + ".view_gui", true);

    public static boolean isFeatureEnabled() {
        return PDAM.getFeatureService().isFeatureEnabled(FEATURE_ID);
    }

    private ForgeListenerBinding[] listeners;

    public SignEditorFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        listeners = ForgeListenerBinding.of(new SignEditorListener());
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        context.getScope().registerBehavior(new SignConfigImGuiRender());
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(listeners);
    }
}
