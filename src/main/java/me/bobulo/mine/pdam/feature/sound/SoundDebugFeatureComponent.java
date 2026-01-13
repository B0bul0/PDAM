package me.bobulo.mine.pdam.feature.sound;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.feature.component.AbstractFeatureComponent;
import me.bobulo.mine.pdam.feature.sound.log.SoundLogEntry;
import me.bobulo.mine.pdam.feature.sound.widow.SoundDebugWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import net.minecraftforge.common.MinecraftForge;

public final class SoundDebugFeatureComponent extends AbstractFeatureComponent {

    private SoundDebugListener listener;
    private SoundDebugWindow window;

    private LogHistory<SoundLogEntry> soundHistory;

    @Override
    protected void onEnable() {
        this.soundHistory = new LogHistory<>();
        this.listener = new SoundDebugListener(this);
        this.window = new SoundDebugWindow(soundHistory);

        MinecraftForge.EVENT_BUS.register(this.listener);
        PDAM.getImGuiRenderer().registerWidow(this.window);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this.listener);
        this.listener = null;

        PDAM.getImGuiRenderer().unregisterWidow(window);
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
