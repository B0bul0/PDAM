package me.bobulo.mine.devmod.feature.sound;

import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoundDebugListener {

    private final SoundDebugFeatureComponent soundDebugFeatureComponent;

    public SoundDebugListener(SoundDebugFeatureComponent soundDebugFeatureComponent) {
        this.soundDebugFeatureComponent = soundDebugFeatureComponent;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerSoundEvent(PlaySoundSourceEvent event) {
        String name = event.name;
        float pitch = event.sound.getPitch();
        float volume = event.sound.getVolume();

        soundDebugFeatureComponent.sendDebugMessage(name, pitch, volume);
    }

}
