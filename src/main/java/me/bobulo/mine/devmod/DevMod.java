package me.bobulo.mine.devmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "devmod", useMetadata = true)
public class DevMod {

    private static final Logger log = LogManager.getLogger(DevMod.class);

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        log.info("DevMod initialized");
    }

}