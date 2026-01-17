package me.bobulo.mine.pdam;

import me.bobulo.mine.pdam.command.CopyToClipboardCommand;
import me.bobulo.mine.pdam.config.ConfigListener;
import me.bobulo.mine.pdam.config.ConfigurationService;
import me.bobulo.mine.pdam.feature.component.ImGuiListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.hologram.HologramWindow;
import me.bobulo.mine.pdam.feature.chat.CharacterMapWindow;
import me.bobulo.mine.pdam.feature.chat.ChatCopyListener;
import me.bobulo.mine.pdam.feature.FeatureImpl;
import me.bobulo.mine.pdam.feature.FeatureService;
import me.bobulo.mine.pdam.feature.component.CallbackFeatureComponent;
import me.bobulo.mine.pdam.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.entity.EntityOverlayInfoListener;
import me.bobulo.mine.pdam.feature.entity.ShowInvisibleEntities;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureComponent;
import me.bobulo.mine.pdam.feature.player.FlyBoosterWindow;
import me.bobulo.mine.pdam.feature.player.SendChatMessageWindow;
import me.bobulo.mine.pdam.feature.scoreboard.ScoreboardInspectorWindow;
import me.bobulo.mine.pdam.feature.server.ServerInfoWindow;
import me.bobulo.mine.pdam.feature.skin.HeadWorldSkinExtractionListener;
import me.bobulo.mine.pdam.feature.skin.HotBarSkinExtractionListener;
import me.bobulo.mine.pdam.feature.skin.PlayerSkinExtractionListener;
import me.bobulo.mine.pdam.feature.sound.SoundDebugFeatureComponent;
import me.bobulo.mine.pdam.feature.tooltop.NBTTagTooltipListener;
import me.bobulo.mine.pdam.imgui.ImGuiRenderer;
import me.bobulo.mine.pdam.notification.NotificationDisplayElement;
import me.bobulo.mine.pdam.ui.UIManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
  modid = PDAM.MOD_ID,
  useMetadata = true,
  guiFactory = "me.bobulo.mine.pdam.gui.DefaultGuiFactory"
)
public final class PDAM {

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

    public static ImGuiRenderer getImGuiRenderer() {
        return instance.imGuiRenderer;
    }

    public static File getConfigDirectory() {
        return instance.configDirectory;
    }

    /* Instance */

    private File configDirectory;
    private ConfigurationService config;
    private FeatureService featureService;
    private UIManager uiManager;
    private ImGuiRenderer imGuiRenderer;

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        if (instance != null) {
            throw new IllegalStateException("PDAM instance already initialized");
        }

        instance = this;

        this.configDirectory = new File(event.getModConfigurationDirectory(), "pdam");
        if (!this.configDirectory.exists()) {
            this.configDirectory.mkdirs();
        }

        this.config = new ConfigurationService();
        this.config.init(new File(configDirectory, "pdam.cfg"));

        this.uiManager = new UIManager();
        this.imGuiRenderer = new ImGuiRenderer();
        this.featureService = new FeatureService();

        uiManager.addElement(new NotificationDisplayElement());

        MinecraftForge.EVENT_BUS.register(new ConfigListener());
        MinecraftForge.EVENT_BUS.register(uiManager);
        MinecraftForge.EVENT_BUS.register(new ChatCopyListener());

        imGuiRenderer.registerWidow(new SendChatMessageWindow());

        // Register client commands
        ClientCommandHandler.instance.registerCommand(new CopyToClipboardCommand());

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

        featureService.registerFeature(FeatureImpl.builder()
          .id("skin_extraction")
          .component(ForgerListenerFeatureComponent.of(
            new PlayerSkinExtractionListener(),
            new HeadWorldSkinExtractionListener(),
            new HotBarSkinExtractionListener()
          ))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("packet_monitor")
          .component(new PacketMonitorFeatureComponent())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("character_map")
          .component(ImGuiListenerFeatureComponent.of(new CharacterMapWindow()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("hologram_mockup")
          .component(ImGuiListenerFeatureComponent.of(new HologramWindow()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("scoreboard_inspector")
          .component(ImGuiListenerFeatureComponent.of(new ScoreboardInspectorWindow()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("server_info")
          .component(ImGuiListenerFeatureComponent.of(new ServerInfoWindow()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("fly_booster")
          .component(ImGuiListenerFeatureComponent.of(new FlyBoosterWindow()))
          .build());
    }

}