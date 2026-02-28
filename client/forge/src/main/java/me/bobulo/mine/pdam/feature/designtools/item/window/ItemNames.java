package me.bobulo.mine.pdam.feature.designtools.item.window;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Utility class to get all item names in Minecraft.
 */
public final class ItemNames {

    static Collection<String> getItemNames() {
        Set<String> items = new HashSet<>();

        Item.itemRegistry.iterator().forEachRemaining(item -> {
            items.add(item.getRegistryName());

            if (item.delegate.getResourceName() != null)
                items.add(item.delegate.getResourceName().getResourcePath());

            int itemId = Item.getIdFromItem(item);

            items.add(String.valueOf(itemId));

            // Add meta variants

            CreativeTabs tab = item.getCreativeTab();

            List<ItemStack> subItems = new ArrayList<>();

            item.getSubItems(item, tab, subItems); // fill subItems with all variants

            int[] metaArray = subItems.stream()
              .mapToInt(ItemStack::getMetadata)
              .filter(meta -> meta != 0) // ignore meta 0
              .toArray();

            for (int i : metaArray) {
                items.add(item.getRegistryName() + ":" + i);

                if (item.delegate.getResourceName() != null)
                    items.add(item.delegate.getResourceName().getResourcePath() + ":" + i);

                items.add(itemId + ":" + i);
            }

        });

        return items;
    }

}
