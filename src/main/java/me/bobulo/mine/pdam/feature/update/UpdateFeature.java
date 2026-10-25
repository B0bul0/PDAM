package me.bobulo.mine.pdam.feature.update;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import org.jetbrains.annotations.NotNull;

public final class UpdateFeature extends AbstractFeature {

    public static final String FEATURE_ID = "update";

    public static final String GITHUB_USER = "B0bul0";
    public static final String GITHUB_REPO = "PDAM";
    public static final String GITHUB_URL_DOWNLOAD = "https://github.com/%s/%s/releases/latest";

    // Disable chat update warning
    public static final ConfigValue<Boolean> CHAT_WARNING = ConfigProperty.of(FEATURE_ID + ".chat_warning", true);

    private UpdateChecker updateChecker;

    public UpdateFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        this.updateChecker = new UpdateChecker(getCurrentVersion());

        UpdateConfigImGuiRender config = new UpdateConfigImGuiRender(updateChecker);
        context.getScope().registerBehavior(config);
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBinding(new ForgeListenerBinding(new ChatUpdateWarning(updateChecker)));
        this.updateChecker.asyncCheckForUpdates();
    }

    private static String getCurrentVersion() {
        ModContainer container = Loader.instance().getIndexedModList().get(PDAM.MOD_ID);

        if (container != null) {
            ModMetadata meta = container.getMetadata();
            return meta.version;
        }

        return "unknown";
    }

}
