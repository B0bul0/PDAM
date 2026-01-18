package me.bobulo.mine.pdam.feature.designtools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.NumberUtils;

public class ItemStackResolver {

    public static ItemStack resolve(String material) {
        Item item = Item.getByNameOrId(material);
        if (item != null) {
            return new ItemStack(item);
        }

        Integer dataId = null;

        String[] parts = material.split(":");
        if (parts.length == 1) {
            item = Item.getByNameOrId("minecraft:" + parts[0]);
        }

        if (parts.length == 2) {

            // Check Item:Number Data
            if (NumberUtils.isDigits(parts[1])) {
                item = Item.getByNameOrId(parts[0]);
                dataId = Integer.parseInt(parts[1]);
            }

        }

        if (parts.length == 3) {
            // Check Item:Number Data
            if (NumberUtils.isDigits(parts[2])) {
                item = Item.getByNameOrId(parts[0] + ":" + parts[1]);
                dataId = Integer.parseInt(parts[2]);
            }

        }

        if (item == null) {
            return null;
        }

        if (dataId != null) {
            return new ItemStack(item, 1, dataId.shortValue());
        }

        return new ItemStack(item);
    }

}
