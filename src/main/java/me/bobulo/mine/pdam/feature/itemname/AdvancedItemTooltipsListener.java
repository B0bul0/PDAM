package me.bobulo.mine.pdam.feature.itemname;

import me.bobulo.mine.pdam.feature.itemname.mapper.ItemMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public final class AdvancedItemTooltipsListener {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (!event.showAdvancedItemTooltips) {
            return;
        }

        ItemMapper itemMapper = ItemNameDebug.getItemMapper();
        if (itemMapper == null) {
            return;
        }

        List<String> toolTip = event.toolTip;
        ItemStack itemStack = event.itemStack;
        String itemName = itemMapper.mapItemName(Item.getIdFromItem(itemStack.getItem()));
        if (itemName == null) {
            return;
        }

        int index = findItemNameLineIndex(toolTip, itemStack);

        if (ItemNameDebug.OVERRIDE_ITEM_NAME.get()) {
            if (index != -1) {
                toolTip.set(index, "§8" + itemName);
            }
            return;
        }

        if (index != -1 && index != toolTip.size() - 1) {
            toolTip.add(index + 1, "§8" + itemName);
        } else {
            toolTip.add("§8" + itemName);
        }

    }

    private int findItemNameLineIndex(List<String> toolTip, ItemStack itemStack) {
        for (int i = toolTip.size() - 1; i >= 0; i--) {
            String line = toolTip.get(i);
            if (line == null || !line.startsWith("§8minecraft:")) {
                continue;
            }

            String currentName = line.substring(2); // substring color
            if (!itemStack.getItem().getRegistryName().equals(currentName)) {
                continue;
            }

            return i;
        }

        return -1;
    }

}
