package me.bobulo.mine.pdam.feature.tooltip;

import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import org.jetbrains.annotations.NotNull;

/**
 * Adds item NBT information to tooltips while enabled.
 */
public final class NBTTooltipFeature extends AbstractFeature {

    public static final String FEATURE_ID = "nbt_tooltip";

    private ForgeListenerBinding[] listeners;

    public NBTTooltipFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        listeners = ForgeListenerBinding.of(new NBTTagTooltipListener());
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(listeners);
    }
}
