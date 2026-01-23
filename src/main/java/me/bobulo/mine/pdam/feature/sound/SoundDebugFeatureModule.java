package me.bobulo.mine.pdam.feature.sound;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRenderer;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.sound.log.SoundLogEntry;
import me.bobulo.mine.pdam.feature.sound.window.SoundDebugWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import net.minecraftforge.common.MinecraftForge;

public final class SoundDebugFeatureModule extends AbstractFeatureModule {

    private SoundDebugListener listener;
    private SoundDebugWindow window;

    private LogHistory<SoundLogEntry> soundHistory;

    @Override
    protected void onEnable() {
        this.soundHistory = new LogHistory<>();
        this.listener = new SoundDebugListener(this);
        this.window = new SoundDebugWindow(soundHistory);

        addChildModule(new ToolbarMenuImGuiRenderer(ImmutableList.of(window)));

        MinecraftForge.EVENT_BUS.register(this.listener);
        PDAM.getImGuiRenderer().registerWindow(this.window);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this.listener);
        this.listener = null;

        PDAM.getImGuiRenderer().unregisterWindow(window);
        this.window = null;

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
