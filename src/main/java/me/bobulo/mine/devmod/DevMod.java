package me.bobulo.mine.devmod;

import me.bobulo.mine.devmod.feature.FeatureImpl;
import me.bobulo.mine.devmod.feature.FeatureService;
import me.bobulo.mine.devmod.feature.sound.SoundDebugFeatureComponent;
import me.bobulo.mine.devmod.feature.tooltop.NBTTagTooltipListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "devmod", useMetadata = true)
public class DevMod {

    private static final Logger log = LogManager.getLogger(DevMod.class);

    private FeatureService featureService;

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        this.featureService = new FeatureService();
        MinecraftForge.EVENT_BUS.register(new NBTTagTooltipListener());

        registerFeatures();
        log.info("DevMod initialized");
    }

    public FeatureService getFeatureService() {
        return featureService;
    }

    private void registerFeatures() {
        featureService.registerFeature(FeatureImpl.builder()
          .id("sound_debug")
          .component(new SoundDebugFeatureComponent())
          .build());
    }

}