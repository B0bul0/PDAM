package me.bobulo.mine.pdam.feature.itemname.mapper;

/**
 * Interface for mapping item names between different formats.
 */
public interface ItemMapper {

    ItemMapper VANILLA = new VanillaItemMapper();
    ItemMapper ITEM_ID = new IdItemMapper();
    ItemMapper LOCALIZED_NAME = new LocalizedNameItemMapper();
    ItemMapper BUKKIT_1_8 = new Bukkit1_8ItemMapper();

    /**
     * Maps an item ID to its corresponding item name.
     *
     * @param minecraftItemId the item ID to map
     * @return the mapped item name
     */
    String mapItemName(int minecraftItemId);

    /**
     * Reverse maps an item name to its corresponding item ID.
     *
     * @param item the item name to reverse map
     * @return the mapped item ID
     */
    int reverseMapItemName(String item);

}
