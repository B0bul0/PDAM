package me.bobulo.mine.pdam.feature.sound;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import me.bobulo.mine.pdam.feature.sound.log.SoundLogEntry;
import me.bobulo.mine.pdam.feature.sound.window.SoundDebugWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import org.jetbrains.annotations.NotNull;


public final class SoundDebugFeatureModule extends AbstractFeatureModule {

    private SoundDebugListener listener;
    private SoundDebugWindow window;
    private ToolbarMenuImGuiRender menu;
    private ForgeListenerBinding listenerBinding;
    private ImGuiListenerBinding windowBinding;

    private LogHistory<SoundLogEntry> soundHistory;

    @Override
    protected void onEnable(@NotNull FeatureModuleEnableContext context) {
        this.soundHistory = new LogHistory<>();
        this.listener = new SoundDebugListener(this);
        this.window = new SoundDebugWindow(soundHistory);

        this.menu = new ToolbarMenuImGuiRender(ImmutableList.of(window));
        context.getScope().registerBehavior(menu);
        this.listenerBinding = new ForgeListenerBinding(listener);
        this.windowBinding = new ImGuiListenerBinding(window);
        context.getScope().registerBindings(listenerBinding, windowBinding);
    }

    @Override
    protected void onDisable(@NotNull FeatureModuleDisableContext context) {
        context.getScope().unregisterBehavior(menu);
        context.getScope().unregisterBinding(listenerBinding);
        context.getScope().unregisterBinding(windowBinding);
        this.listener = null;
        this.window = null;
        this.menu = null;

        this.soundHistory.clear();
        this.soundHistory = null;
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
