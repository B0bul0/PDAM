package me.bobulo.mine.pdam.feature.creative;

import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.NotNull;

/**
 * Feature that expands the creative inventory by adding certain blocks to specific creative tabs.
 */
public final class ExpandedCreativeInventoryFeature extends AbstractFeature {

    public static final String FEATURE_ID = "expanded_creative_inventory";

    public ExpandedCreativeInventoryFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        Blocks.barrier.setCreativeTab(CreativeTabs.tabMisc);
        Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
        Blocks.dragon_egg.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    protected void onDisable(@NotNull FeatureDisableContext context) {
        Blocks.barrier.setCreativeTab(null);
        Blocks.command_block.setCreativeTab(null);
        Blocks.dragon_egg.setCreativeTab(null);
    }

}
