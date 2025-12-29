package me.bobulo.mine.pdam.feature.sound;

import me.bobulo.mine.pdam.util.TextComponentBuilder;
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
        String soundName = event.name;
        float pitch = event.sound.getPitch();
        float volume = event.sound.getVolume();

        if (!soundDebugFeatureComponent.filter(soundName)) {
            return;
        }

        TextComponentBuilder
          .createTranslated("pdam.sound_debug.debug_message",
            soundName, String.format("%.2f", pitch), String.format("%.2f", volume))
          .withHoverTranslated("pdam.sound_debug.debug_message_hover_location",
            event.sound.getXPosF(), event.sound.getYPosF(), event.sound.getZPosF()
          ).sendToClient();
    }

}
