package me.bobulo.mine.devmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "devmod", useMetadata = true)
public class DevMod {

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        System.out.println("DevMod initialized");
    }

}