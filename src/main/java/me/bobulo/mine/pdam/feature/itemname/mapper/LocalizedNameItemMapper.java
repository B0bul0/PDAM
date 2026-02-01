package me.bobulo.mine.pdam.feature.itemname.mapper;

import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;

public final class LocalizedNameItemMapper implements ItemMapper {

    @Override
    public String mapItemName(int minecraftItemId) {
        Item item = Item.getItemById(minecraftItemId);
        if (item == null) {
            return null;
        }

        return StatCollector.translateToLocal(item.getUnlocalizedName() + ".name").trim();
    }

    @Override
    public int reverseMapItemName(String item) {
        for (int id = 0; id < Short.MAX_VALUE; id++) {
            Item minecraftItem = Item.getItemById(id);
            if (minecraftItem == null) {
                continue;
            }

            String localizedName = StatCollector.translateToLocal(minecraftItem.getUnlocalizedName() + ".name").trim();
            if (localizedName.equalsIgnoreCase(item.trim())) {
                return id;
            }
        }

        return -1;
    }

}
