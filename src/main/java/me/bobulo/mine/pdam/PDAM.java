package me.bobulo.mine.pdam;

import me.bobulo.mine.pdam.command.CopyToClipboardCommand;
import me.bobulo.mine.pdam.config.ConfigService;
import me.bobulo.mine.pdam.feature.FeatureService;
import me.bobulo.mine.pdam.feature.bungeebypass.BungeeBypassFeature;
import me.bobulo.mine.pdam.feature.chat.MessagingUtilitiesFeature;
import me.bobulo.mine.pdam.feature.chunk.ChunkViewerFeature;
import me.bobulo.mine.pdam.feature.creative.ExpandedCreativeInventoryFeature;
import me.bobulo.mine.pdam.feature.designtools.DesignToolsFeature;
import me.bobulo.mine.pdam.feature.entity.EntityInfoOverlayFeature;
import me.bobulo.mine.pdam.feature.entity.ShowInvisibleEntitiesFeature;
import me.bobulo.mine.pdam.feature.hitbox.HitBoxesFeature;
import me.bobulo.mine.pdam.feature.inventoryslot.InventorySlotInspectorFeature;
import me.bobulo.mine.pdam.feature.itemname.ItemNameDebugFeature;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeature;
import me.bobulo.mine.pdam.feature.player.FlyBoosterFeature;
import me.bobulo.mine.pdam.feature.player.PlayerUtilitiesFeature;
import me.bobulo.mine.pdam.feature.scoreboard.ScoreboardInspectorFeature;
import me.bobulo.mine.pdam.feature.server.ServerInfoFeature;
import me.bobulo.mine.pdam.feature.sign.SignEditorFeature;
import me.bobulo.mine.pdam.feature.signfinder.SignFinderFeature;
import me.bobulo.mine.pdam.feature.skin.SkinExtractionFeature;
import me.bobulo.mine.pdam.feature.sound.SoundDebugFeature;
import me.bobulo.mine.pdam.feature.tooltip.NBTTooltipFeature;
import me.bobulo.mine.pdam.feature.update.UpdateFeature;
import me.bobulo.mine.pdam.feature.world.WorldTimeFeature;
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

        log.info("PDAM initialized");
    }

    private void registerFeatures() {
        featureService.registerFeature(new SoundDebugFeature());
        featureService.registerFeature(new NBTTooltipFeature());
        featureService.registerFeature(new ShowInvisibleEntitiesFeature());
        featureService.registerFeature(new EntityInfoOverlayFeature());
        featureService.registerFeature(new SkinExtractionFeature());
        featureService.registerFeature(new PacketMonitorFeature());
        featureService.registerFeature(new ScoreboardInspectorFeature());
        featureService.registerFeature(new ServerInfoFeature());
        featureService.registerFeature(new FlyBoosterFeature());
        featureService.registerFeature(new MessagingUtilitiesFeature());
        featureService.registerFeature(new SignEditorFeature());
        featureService.registerFeature(new DesignToolsFeature());
        featureService.registerFeature(new InventorySlotInspectorFeature());
        featureService.registerFeature(new ExpandedCreativeInventoryFeature());
        featureService.registerFeature(new PlayerUtilitiesFeature());
        featureService.registerFeature(new ItemNameDebugFeature());
        featureService.registerFeature(new WorldTimeFeature());
        featureService.registerFeature(new BungeeBypassFeature());
        featureService.registerFeature(new SignFinderFeature());
        featureService.registerFeature(new HitBoxesFeature());
        featureService.registerFeature(new ChunkViewerFeature());
        featureService.registerFeature(new UpdateFeature());
    }

}
