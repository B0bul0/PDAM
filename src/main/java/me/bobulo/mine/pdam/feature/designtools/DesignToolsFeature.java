package me.bobulo.mine.pdam.feature.designtools;

import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.designtools.charactermap.CharacterMapWindow;
import me.bobulo.mine.pdam.feature.designtools.hologram.HologramMockupWindow;
import me.bobulo.mine.pdam.feature.designtools.item.window.ItemBuilderWindow;
import org.jetbrains.annotations.NotNull;

/**
 * Groups design tool windows and their toolbar entries.
 */
public final class DesignToolsFeature extends AbstractFeature {

    public static final String FEATURE_ID = "design_tools";

    private ImGuiListenerBinding[] windows;

    public DesignToolsFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        windows = ImGuiListenerBinding.of(
          new CharacterMapWindow(),
          new MessageFormatterWindow(),
          new TitleVisualizerWindow(),
          new ActionBarVisualizerWindow(),
          new HologramMockupWindow(),
          new PlaySoundWindow(),
          new ItemBuilderWindow(),
          new SpawnParticleWindow()
        );
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        context.getScope().registerBehavior(new DesignToolsMenuImguiRender());
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(windows);
    }
}
