package me.bobulo.mine.pdam.feature.update;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.feature.Feature;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.ForgerListenerFeatureModule;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public final class UpdateFeatureModule extends AbstractFeatureModule {

    private UpdateChecker updateChecker;

    @Override
    protected void onInitialize() {
        Feature feature = getFeature();

        this.updateChecker = new UpdateChecker(getCurrentVersion());

        feature.addModule(ForgerListenerFeatureModule.of(
          new ChatUpdateWarning(updateChecker)
        ));
        feature.addModule(new UpdateConfigImGuiRender(updateChecker));
    }

    @Override
    protected void onEnable() {
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
