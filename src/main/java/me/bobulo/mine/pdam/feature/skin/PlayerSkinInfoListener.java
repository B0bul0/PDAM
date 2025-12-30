package me.bobulo.mine.pdam.feature.skin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Listener for right-clicking on players to retrieve skin information.
 */
public class PlayerSkinInfoListener {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        Entity target = event.target;
        if (!(target instanceof EntityPlayer)) {
            return;
        }

        // Cancel the default interaction
        event.setResult(Event.Result.DENY);
        event.setCanceled(true);

        EntityPlayer targetPlayer = (EntityPlayer) target;
        GameProfile gameProfile = targetPlayer.getGameProfile();
        SkinInfoUtils.sendSkinInfoMessage(gameProfile);
    }

}