package me.bobulo.mine.pdam.feature.sound;

import me.bobulo.mine.pdam.feature.sound.log.SoundLogEntry;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoundDebugListener {

    private final SoundDebugFeature soundDebugFeature;

    public SoundDebugListener(SoundDebugFeature soundDebugFeature) {
        this.soundDebugFeature = soundDebugFeature;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerSoundEvent(PlaySoundSourceEvent event) {
        String soundName = event.sound.getSoundLocation().toString();
        float pitch = event.sound.getPitch();
        float volume = event.sound.getVolume();

        soundDebugFeature.logSound(new SoundLogEntry(
            soundName, volume, pitch,
          event.sound.getXPosF(), event.sound.getYPosF(), event.sound.getZPosF()
        ));
    }

}
