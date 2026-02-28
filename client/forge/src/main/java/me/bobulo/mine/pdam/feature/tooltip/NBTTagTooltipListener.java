package me.bobulo.mine.pdam.feature.tooltip;

import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public class NBTTagTooltipListener {

    private static final Set<String> excludedTags = Sets.newHashSet(
      "display",
      "SkullOwner"
    );

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

        toolTip.add("");
        toolTip.add(EnumChatFormatting.DARK_GRAY + "NBT Data:");

        for (String key : nbt.getKeySet()) {
            if (excludedTags.contains(key)) {
                continue;
            }

            NBTBase tag = nbt.getTag(key);
            toolTip.add(EnumChatFormatting.GRAY + " " + key + ": " + tag.toString());
        }
    }

}