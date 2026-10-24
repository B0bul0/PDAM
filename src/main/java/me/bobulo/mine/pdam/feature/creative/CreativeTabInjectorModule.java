package me.bobulo.mine.pdam.feature.creative;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.NotNull;

/**
 * Feature module that injects certain blocks into specific creative tabs.
 */
public final class CreativeTabInjectorModule extends AbstractFeatureModule {

    @Override
    protected void onEnable(@NotNull FeatureModuleEnableContext context) {
        Blocks.barrier.setCreativeTab(CreativeTabs.tabMisc);
        Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
        Blocks.dragon_egg.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    protected void onDisable(@NotNull FeatureModuleDisableContext context) {
        Blocks.barrier.setCreativeTab(null);
        Blocks.command_block.setCreativeTab(null);
        Blocks.dragon_egg.setCreativeTab(null);
    }

}
