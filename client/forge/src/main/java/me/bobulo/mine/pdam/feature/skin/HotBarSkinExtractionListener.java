package me.bobulo.mine.pdam.feature.skin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Listener for right-clicking with a player skull in hand to retrieve skin information.
 */
public class HotBarSkinExtractionListener {

    private static final long INTERACTION_COOLDOWN_MS = 1000;
    private long lastInteractionTime = 0;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack heldItem = player.getHeldItem();

        // check if the held item is a player skull
        if (heldItem == null || heldItem.getItem() != Items.skull || heldItem.getMetadata() != 3) {
            return;
        }

        if (!heldItem.hasTagCompound() || !heldItem.getTagCompound().hasKey("SkullOwner", 10)) {
            return;
        }

        event.setCanceled(true);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInteractionTime < INTERACTION_COOLDOWN_MS) {
            // Ignore interaction if within cooldown period
            return;
        }

        lastInteractionTime = currentTime;

        NBTTagCompound skullOwnerTag = heldItem.getTagCompound().getCompoundTag("SkullOwner");
        GameProfile gameProfile = NBTUtil.readGameProfileFromNBT(skullOwnerTag);

        SkinExtractionUtils.sendSkinInfoMessage(gameProfile);
    }

}