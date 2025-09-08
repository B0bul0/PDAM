package me.bobulo.mine.devmod.feature.tooltop;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NBTTagTooltipListener {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        List<String> toolTip = event.toolTip;
        ItemStack itemStack = event.itemStack;

        if (!itemStack.hasTagCompound()) {
            return;
        }

        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt == null || nbt.getKeySet().isEmpty()) {
            return;
        }

        NBTTagCompound extraAttributes = nbt.getCompoundTag("ExtraAttributes");

        if (extraAttributes == null || extraAttributes.getKeySet().isEmpty()) {
            return;
        }

        toolTip.add("");
        toolTip.add(EnumChatFormatting.DARK_GRAY + "NBT Data:");

        for (String key : extraAttributes.getKeySet()) {
            NBTBase tag = extraAttributes.getTag(key);

            toolTip.add(EnumChatFormatting.GRAY + " " + key + ": " + tag.toString());
        }
    }

}