package me.bobulo.mine.pdam;

import me.bobulo.mine.pdam.command.CopyToClipboardCommand;
import me.bobulo.mine.pdam.config.ConfigService;
import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.FeatureImpl;
import me.bobulo.mine.pdam.feature.FeatureService;
import me.bobulo.mine.pdam.feature.chat.ChatCopyListener;
import me.bobulo.mine.pdam.feature.chat.window.SendChatMessageWindow;
import me.bobulo.mine.pdam.feature.designtools.*;
import me.bobulo.mine.pdam.feature.designtools.charactermap.CharacterMapWindow;
import me.bobulo.mine.pdam.feature.designtools.hologram.HologramMockupWindow;
import me.bobulo.mine.pdam.feature.entity.EntityOverlayInfoListener;
import me.bobulo.mine.pdam.feature.entity.ShowInvisibleEntities;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.CallbackFeatureModule;
import me.bobulo.mine.pdam.feature.module.ForgerListenerFeatureModule;
import me.bobulo.mine.pdam.feature.module.ImGuiListenerFeatureModule;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeatureModule;
import me.bobulo.mine.pdam.feature.player.FlyBoosterWindow;
import me.bobulo.mine.pdam.feature.scoreboard.ScoreboardInspectorWindow;
import me.bobulo.mine.pdam.feature.server.ServerInfoWindow;
import me.bobulo.mine.pdam.feature.sign.SignEditorListener;
import me.bobulo.mine.pdam.feature.skin.HeadWorldSkinExtractionListener;
import me.bobulo.mine.pdam.feature.skin.HotBarSkinExtractionListener;
import me.bobulo.mine.pdam.feature.skin.PlayerSkinExtractionListener;
import me.bobulo.mine.pdam.feature.sound.SoundDebugFeatureModule;
import me.bobulo.mine.pdam.feature.tooltip.NBTTagTooltipListener;
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
  useMetadata = true
)
public final class PDAM {

    private static final Logger log = LogManager.getLogger(PDAM.class);
    public static final String MOD_ID = "pdam";

    private static PDAM instance;

    public static FeatureService getFeatureService() {
        return instance.featureService;
    }

    public static ConfigService getConfigService() {
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
    private ConfigService config;
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

        this.config = new ConfigService();
        this.config.init(new File(configDirectory, "pdam.json"));

        this.uiManager = new UIManager();
        this.imGuiRenderer = new ImGuiRenderer();
        this.featureService = new FeatureService();

        uiManager.addElement(new NotificationDisplayElement());

        MinecraftForge.EVENT_BUS.register(uiManager);

        // Register client commands
        ClientCommandHandler.instance.registerCommand(new CopyToClipboardCommand());

        registerFeatures();

        for (Feature feature : featureService.getSortedFeatures()) {
            try {
                feature.enable();
            } catch (Exception exception) {
                log.error("Failed to initialize feature: {}", feature.getId(), exception);
            }
        }

        log.info("PDAM initialized");
    }

    private void registerFeatures() {
        featureService.registerFeature(FeatureImpl.builder()
          .id("sound_debug")
          .module(new SoundDebugFeatureModule())
          .module(new ToolbarMenuImGuiRender())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("nbt_tooltip")
          .module(ForgerListenerFeatureModule.of(new NBTTagTooltipListener()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("show_invisible_entities")
          .module(CallbackFeatureModule.of(
            () -> ShowInvisibleEntities.showInvisibleEntities = true,
            () -> ShowInvisibleEntities.showInvisibleEntities = false)
          ).build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("entity_info_overlay")
          .module(ForgerListenerFeatureModule.of(new EntityOverlayInfoListener()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("skin_extraction")
          .module(ForgerListenerFeatureModule.of(
            new PlayerSkinExtractionListener(),
            new HeadWorldSkinExtractionListener(),
            new HotBarSkinExtractionListener()
          ))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("packet_monitor")
          .module(new PacketMonitorFeatureModule())
          .module(new ToolbarMenuImGuiRender())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("scoreboard_inspector")
          .module(ImGuiListenerFeatureModule.of(new ScoreboardInspectorWindow()))
          .module(new ToolbarMenuImGuiRender())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("server_info")
          .module(ImGuiListenerFeatureModule.of(new ServerInfoWindow()))
          .module(new ToolbarMenuImGuiRender())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("fly_booster")
          .module(ImGuiListenerFeatureModule.of(new FlyBoosterWindow()))
          .module(new ToolbarMenuImGuiRender())
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("messaging_utilities")
          .module(ImGuiListenerFeatureModule.of(
            new SendChatMessageWindow()
          ))
          .module(new ToolbarMenuImGuiRender())
          .module(ForgerListenerFeatureModule.of(new ChatCopyListener()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("sign_editor")
          .module(ForgerListenerFeatureModule.of(new SignEditorListener()))
          .build());

        featureService.registerFeature(FeatureImpl.builder()
          .id("design_tools")
          .module(ImGuiListenerFeatureModule.of(
            new CharacterMapWindow(),
            new MessageFormatterWindow(),
            new TitleVisualizerWindow(),
            new ActionBarVisualizerWindow(),
            new HologramMockupWindow(),
            new PlaySoundWindow()
          ))
          .module(new DesignToolsMenuImguiRender())
          .build());
    }

}