package me.bobulo.mine.pdam.feature.designtools.item;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Enchantment {

    PROTECTION("protection", "minecraft:protection"),
    FIRE_PROTECTION("fire_protection", "minecraft:fire_protection"),
    FEATHER_FALLING("feather_falling", "minecraft:feather_falling"),
    BLAST_PROTECTION("blast_protection", "minecraft:blast_protection"),
    PROJECTILE_PROTECTION("projectile_protection", "minecraft:projectile_protection"),
    RESPIRATION("respiration", "minecraft:respiration"),
    AQUA_AFFINITY("aqua_affinity", "minecraft:aqua_affinity"),
    THORNS("thorns", "minecraft:thorns"),
    DEPTH_STRIDER("depth_strider", "minecraft:depth_strider"),
    FROST_WALKER("frost_walker", "minecraft:frost_walker"),
    SHARPNESS("sharpness", "minecraft:sharpness"),
    SMITE("smite", "minecraft:smite"),
    BANE_OF_ARTHROPODS("bane_of_arthropods", "minecraft:bane_of_arthropods"),
    KNOCKBACK("knockback", "minecraft:knockback"),
    FIRE_ASPECT("fire_aspect", "minecraft:fire_aspect"),
    LOOTING("looting", "minecraft:looting"),
    EFFICIENCY("efficiency", "minecraft:efficiency"),
    SILK_TOUCH("silk_touch", "minecraft:silk_touch"),
    UNBREAKING("unbreaking", "minecraft:unbreaking"),
    FORTUNE("fortune", "minecraft:fortune"),
    POWER("power", "minecraft:power"),
    PUNCH("punch", "minecraft:punch"),
    FLAME("flame", "minecraft:flame"),
    INFINITY("infinity", "minecraft:infinity"),
    LUCK_OF_THE_SEA("luck_of_the_sea", "minecraft:luck_of_the_sea"),
    LURE("lure", "minecraft:lure"),
    MENDING("mending", "minecraft:mending"),
    SWEEPING("sweeping", "minecraft:sweeping"),
    IMPALING("impaling", "minecraft:impaling"),
    RIPTIDE("riptide", "minecraft:riptide"),
    LOYALTY("loyalty", "minecraft:loyalty"),
    CHANNELING("channeling", "minecraft:channeling"),
    MULTISHOT("multishot", "minecraft:multishot"),
    PIERCING("piercing", "minecraft:piercing"),
    QUICK_CHARGE("quick_charge", "minecraft:quick_charge"),
    SOUL_SPEED("soul_speed", "minecraft:soul_speed"),
    CURSE_BINDING("binding_curse", "minecraft:binding_curse"),
    CURSE_VANISHING("vanishing_curse", "minecraft:vanishing_curse");

    private static final Map<Integer, Enchantment> BY_ID = new HashMap<>();
    private static final Map<String, Enchantment> BY_KEY = new HashMap<>();
    public static final List<Enchantment> VALUES = ImmutableList.copyOf(values());

    static {
        for (Enchantment enchantment : values()) {
            if (enchantment.id >= 0) {
                BY_ID.put(enchantment.id, enchantment);
            }
            BY_KEY.put(enchantment.key, enchantment);
            BY_KEY.put(enchantment.resourceLocation, enchantment);
        }
    }

    // Local key
    private final String key;

    // Minecraft resourceLocation
    private final String resourceLocation;

    // Minecraft id
    private final int id;

    Enchantment(String key, String resourceLocation) {
        this.key = key;
        this.resourceLocation = resourceLocation;
        this.id = resolveId(key);
    }

    public String getKey() {
        return key;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public int getId() {
        return id;
    }

    public static Enchantment byId(int id) {
        return BY_ID.get(id);
    }

    public static Enchantment byKey(String keyOrResource) {
        return BY_KEY.get(keyOrResource);
    }

    private static int resolveId(String key) {
        try {
            net.minecraft.enchantment.Enchantment target = net.minecraft.enchantment.Enchantment.getEnchantmentByLocation(key);
            if (target == null) {
                return -1;
            }

            for (int i = 0; i < 256; i++) {
                net.minecraft.enchantment.Enchantment e = net.minecraft.enchantment.Enchantment.getEnchantmentById(i);
                if (e != null && e == target) {
                    return i;
                }
            }
        } catch (Exception ignored) {

        }

        return -1;
    }

}
