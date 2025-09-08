package me.bobulo.mine.devmod.feature.sound;

import me.bobulo.mine.devmod.feature.component.AbstractFeatureComponent;
import net.minecraftforge.common.MinecraftForge;

public class SoundDebugFeatureComponent extends AbstractFeatureComponent {

    private SoundDebugListener listener;

    @Override
    public void onEnable() {
        if (this.listener == null) {
            this.listener = new SoundDebugListener();
        }

        MinecraftForge.EVENT_BUS.register(this.listener);
    }

    @Override
    public void onDisable() {
        if (this.listener == null) {
            return;
        }

        MinecraftForge.EVENT_BUS.unregister(this.listener);
        this.listener = null;
    }
}
