package me.bobulo.mine.pdam;

import me.bobulo.mine.pdam.config.ConfigListener;
import me.bobulo.mine.pdam.config.ConfigurationService;
import me.bobulo.mine.pdam.feature.FeatureImpl;
import me.bobulo.mine.pdam.feature.FeatureService;
import me.bobulo.mine.pdam.feature.component.CallbackFeatureComponent;
import me.bobulo.mine.pdam.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.entity.EntityOverlayInfoListener;
import me.bobulo.mine.pdam.feature.entity.ShowInvisibleEntities;
import me.bobulo.mine.pdam.feature.sound.SoundDebugFeatureComponent;
import me.bobulo.mine.pdam.feature.tooltop.NBTTagTooltipListener;
import me.bobulo.mine.pdam.gui.MenuListener;
import me.bobulo.mine.pdam.ui.UIManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
  modid = PDAM.MOD_ID,
  useMetadata = true,
  guiFactory = "me.bobulo.mine.pdam.gui.DefaultGuiFactory"
)
public class PDAM {

    private static final Logger log = LogManager.getLogger(PDAM.class);
    public static final String MOD_ID = "pdam";

    private static PDAM instance;

    public static FeatureService getFeatureService() {
        return instance.featureService;
    }

    public static ConfigurationService getConfigService() {
        return instance.config;
    }

    public static UIManager getUIManager() {
        return instance.uiManager;
    }

    /* Instance */

    private ConfigurationService config;
    private FeatureService featureService;
    private UIManager uiManager;

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        instance = this;

        this.config = new ConfigurationService();
        this.config.init(event.getSuggestedConfigurationFile());

        this.uiManager = new UIManager();
        this.featureService = new FeatureService();

        MinecraftForge.EVENT_BUS.register(new MenuListener());
        MinecraftForge.EVENT_BUS.register(new ConfigListener());
        MinecraftForge.EVENT_BUS.register(uiManager);

        registerFeatures();

        log.info("PDAM initialized");
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

        featureService.registerFeature(FeatureImpl.builder()
          .id("show_invisible_entities")
          .component(CallbackFeatureComponent.of(
            () -> ShowInvisibleEntities.showInvisibleEntities = true,
            () -> ShowInvisibleEntities.showInvisibleEntities = false)
          ).build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("entity_info_overlay")
          .component(ForgerListenerFeatureComponent.of(new EntityOverlayInfoListener()))
          .build());
    }

}