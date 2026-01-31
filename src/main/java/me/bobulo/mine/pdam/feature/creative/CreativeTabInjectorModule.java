package me.bobulo.mine.pdam.feature.creative;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

/**
 * Feature module that injects certain blocks into specific creative tabs.
 */
public final class CreativeTabInjectorModule extends AbstractFeatureModule {

    @Override
    protected void onEnable() {
        Blocks.barrier.setCreativeTab(CreativeTabs.tabMisc);
        Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
        Blocks.dragon_egg.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    protected void onDisable() {
        Blocks.barrier.setCreativeTab(null);
        Blocks.command_block.setCreativeTab(null);
        Blocks.dragon_egg.setCreativeTab(null);
    }

}
