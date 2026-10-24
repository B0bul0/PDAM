package me.bobulo.mine.pdam.feature.update;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleAttachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import org.jetbrains.annotations.NotNull;

public final class UpdateFeatureModule extends AbstractFeatureModule {

    private UpdateChecker updateChecker;
    private UpdateConfigImGuiRender config;
    private ForgeListenerBinding listenerBinding;

    @Override
    protected void onInitialize(@NotNull FeatureModuleAttachContext context) {
        this.updateChecker = new UpdateChecker(getCurrentVersion());

        this.config = new UpdateConfigImGuiRender(updateChecker);
        context.getScope().registerBehavior(config);
    }

    @Override
    protected void onUninitialize(@NotNull FeatureModuleDetachContext context) {
        context.getScope().unregisterBehavior(config);
    }

    @Override
    protected void onDisable(@NotNull FeatureModuleDisableContext context) {
        context.getScope().unregisterBinding(listenerBinding);
    }

    @Override
    protected void onEnable(@NotNull FeatureModuleEnableContext context) {
        listenerBinding = new ForgeListenerBinding(new ChatUpdateWarning(updateChecker));
        context.getScope().registerBinding(listenerBinding);
        this.updateChecker.asyncCheckForUpdates();
    }

    private String getCurrentVersion() {
        ModContainer container = Loader.instance().getIndexedModList().get(PDAM.MOD_ID);

        if (container != null) {
            ModMetadata meta = container.getMetadata();
            return meta.version;
        }

        return "unknown";
    }

}
