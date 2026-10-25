package me.bobulo.mine.pdam.feature.sound;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRender;
import me.bobulo.mine.pdam.feature.sound.log.SoundLogEntry;
import me.bobulo.mine.pdam.feature.sound.window.SoundDebugWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import org.jetbrains.annotations.NotNull;

public final class SoundDebugFeature extends AbstractFeature {

    public static final String FEATURE_ID = "sound_debug";

    private LogHistory<SoundLogEntry> soundHistory;
    private ForgeListenerBinding listenerBinding;
    private ImGuiListenerBinding windowBinding;

    public SoundDebugFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        this.soundHistory = new LogHistory<>();
        SoundDebugListener listener = new SoundDebugListener(this);
        SoundDebugWindow window = new SoundDebugWindow(soundHistory);

        ToolbarMenuImGuiRender menu = new ToolbarMenuImGuiRender(ImmutableList.of(window));
        context.getScope().registerBehavior(menu);
        listenerBinding = new ForgeListenerBinding(listener);
        windowBinding = new ImGuiListenerBinding(window);
        context.getScope().registerBindings(listenerBinding, windowBinding);
    }

    @Override
    protected void onDisable(@NotNull FeatureDisableContext context) {
        try {
            if (context.getScope().getBindings().contains(listenerBinding)) {
                context.getScope().unregisterBinding(listenerBinding);
            }
            if (context.getScope().getBindings().contains(windowBinding)) {
                context.getScope().unregisterBinding(windowBinding);
            }
        } finally {
            listenerBinding = null;
            windowBinding = null;
            if (soundHistory != null) {
                soundHistory.clear();
                soundHistory = null;
            }
        }
    }

    public LogHistory<SoundLogEntry> getSoundHistory() {
        return soundHistory;
    }

    public void logSound(SoundLogEntry soundLogEntry) {
        if (soundHistory != null) {
            soundHistory.addLogEntry(soundLogEntry);
        }
    }

}
