package me.bobulo.mine.pdam.feature.itemname.mapper;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Minecraft item name <-> item ID mapper.
 */
public final class VanillaItemMapper implements ItemMapper {

    @Override
    public String mapItemName(int minecraftItemId) {
        Item item = Item.getItemById(minecraftItemId);
        if (item == null) {
            return null;
        }

        ResourceLocation nameForObject = Item.itemRegistry.getNameForObject(item);
        return nameForObject != null ? nameForObject.toString() : null;
    }

    @Override
    public int reverseMapItemName(String item) {
        Item minecraftItem = Item.getByNameOrId(item);
        if (minecraftItem == null) {
            return -1;
        }

        return Item.getIdFromItem(minecraftItem);
    }

}
