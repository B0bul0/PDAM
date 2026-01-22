package me.bobulo.mine.pdam.feature.designtools;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.context.FeatureContext;
import me.bobulo.mine.pdam.feature.designtools.charactermap.CharacterMapWindow;
import me.bobulo.mine.pdam.feature.designtools.hologram.HologramMockupWindow;
import me.bobulo.mine.pdam.feature.designtools.item.window.ItemBuilderWindow;
import me.bobulo.mine.pdam.feature.module.ImGuiListenerFeatureModule;

/**
 * Context for the Design Tools feature.
 */
public final class DesignToolsContext extends FeatureContext {

    // Singleton instance

    private static DesignToolsContext instance;

    public static DesignToolsContext get() {
        if (instance == null) {
            instance = new DesignToolsContext();
        }
        return instance;
    }

    public static final ConfigValue<Boolean> ENABLED = ConfigProperty.of("design_tools.enabled", true)
      .onChange(enabled -> {
            if (enabled) {
                get().getFeature().enable();
            } else {
                get().getFeature().disable();
            }
      })
      .sync();

    private DesignToolsContext() {
        super("design_tools");
    }

    @Override
    protected void setup() {
        addModules(
          ImGuiListenerFeatureModule.of(
            new CharacterMapWindow(),
            new MessageFormatterWindow(),
            new TitleVisualizerWindow(),
            new ActionBarVisualizerWindow(),
            new HologramMockupWindow(),
            new PlaySoundWindow(),
            new ItemBuilderWindow()
          ),
          new DesignToolsMenuImguiRender(this)
        );
    }

}
