package me.bobulo.mine.devmod;

import me.bobulo.mine.devmod.config.ConfigListener;
import me.bobulo.mine.devmod.config.ConfigurationService;
import me.bobulo.mine.devmod.feature.FeatureImpl;
import me.bobulo.mine.devmod.feature.FeatureService;
import me.bobulo.mine.devmod.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.devmod.feature.sound.SoundDebugFeatureComponent;
import me.bobulo.mine.devmod.feature.tooltop.NBTTagTooltipListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
  modid = DevMod.MOD_ID,
  useMetadata = true,
  guiFactory = "me.bobulo.mine.devmod.gui.DefaultGuiFactory"
)
public class DevMod {

    private static final Logger log = LogManager.getLogger(DevMod.class);
    public static final String MOD_ID = "devmod";

    private static DevMod instance;

    public static FeatureService getFeatureService() {
        return instance.featureService;
    }

    public static ConfigurationService getConfigService() {
        return instance.config;
    }

    /* Instance */

    private ConfigurationService config;
    private FeatureService featureService;

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        instance = this;

        this.config = new ConfigurationService();
        this.config.init(event.getSuggestedConfigurationFile());

        this.featureService = new FeatureService();

        MinecraftForge.EVENT_BUS.register(new NBTTagTooltipListener());
        MinecraftForge.EVENT_BUS.register(new ConfigListener());

        registerFeatures();

        log.info("DevMod initialized");
    }

    private void registerFeatures() {
        featureService.registerFeature(FeatureImpl.builder()
          .id("sound_debug")
          .component(new SoundDebugFeatureComponent())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("nbt_tooltip")
          .component(ForgerListenerFeatureComponent.of(new NBTTagTooltipListener()))
          .build());
    }

}