package me.bobulo.mine.devmod.feature.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoundDebugListener {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerSoundEvent(PlaySoundSourceEvent event) {
        String name = event.name;
        float pitch = event.sound.getPitch();
        float volume = event.sound.getVolume();

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        String message = String.format("§8Playing sound §a%s§8 with pitch §a%.2f§8 and volume §a%.2f",
          name, pitch, volume);
        player.addChatMessage(new ChatComponentText(message));
    }

}
