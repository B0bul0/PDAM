package me.bobulo.mine.pdam.feature.itemname;

import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;
import me.bobulo.mine.pdam.feature.itemname.mapper.ItemMapper;

/**
 * Feature for mapping Minecraft item names using different mappers.
 */
public final class ItemNameDebug {

    public static final String FEATURE_ID = "item_name_debug";

    /**
     * The mapper configuration to use for mapping item names.
     */
    public static final ConfigValue<String> MAPPER_CONFIG = ConfigProperty.of(FEATURE_ID + ".mapper", "BUKKIT_1_8");

    /**
     * If true, the original item name in the tooltip will be replaced with the mapped name.
     * If false, the mapped name will be added below the original name.
     */
    public static final ConfigValue<Boolean> OVERRIDE_ITEM_NAME = ConfigProperty.of(FEATURE_ID + ".override_name", true);

    /**
     * Retrieves the ItemMapper instance based on the current configuration.
     */
    public static ItemMapper getItemMapper() {
        String mapperName = MAPPER_CONFIG.get();
        switch (mapperName) {
            case "VANILLA":
                return ItemMapper.VANILLA;
            case "ITEM_ID":
                return ItemMapper.ITEM_ID;
            case "LOCALIZED_NAME":
                return ItemMapper.LOCALIZED_NAME;
            case "BUKKIT_1_8":
                return ItemMapper.BUKKIT_1_8;
            default:
                return null;
        }
    }

}
