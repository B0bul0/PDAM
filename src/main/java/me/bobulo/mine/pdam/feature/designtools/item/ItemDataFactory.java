package me.bobulo.mine.pdam.feature.designtools.item;

import com.google.gson.Gson;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ItemDataFactory {

    private static final Gson GSON = new Gson();

    @NotNull
    public static ItemData fromJson(@NotNull String json) {
        return GSON.fromJson(json, ItemData.class);
    }

    @NotNull
    public static String toJson(@NotNull ItemData itemData) {
        return GSON.toJson(itemData, ItemData.class);
    }

    @NotNull
    public static ItemData fromItemStack(@NotNull ItemStack itemStack) {
        ItemData itemBuilder = new ItemData();

        itemBuilder.setMaterial(Item.itemRegistry.getNameForObject(itemStack.getItem()).toString() +
          ":" + itemStack.getItemDamage());

        itemBuilder.setAmount(itemStack.stackSize);

        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound != null) {
            NBTTagCompound displayTag = tagCompound.getCompoundTag("display");

            if (displayTag != null && displayTag.hasKey("Name")) {
                itemBuilder.setCustomName(displayTag.getString("Name"));
            }

            if (displayTag != null && displayTag.hasKey("Lore")) {
                NBTTagList loreTag = displayTag.getTagList("Lore", 8);
                StringBuilder loreBuilder = new StringBuilder();
                for (int i = 0; i < loreTag.tagCount(); i++) {
                    if (i > 0) {
                        loreBuilder.append("\n");
                    }
                    loreBuilder.append(loreTag.getStringTagAt(i));
                }
                itemBuilder.setCustomLore(loreBuilder.toString());
            }

            if (tagCompound.hasKey("Unbreakable")) {
                itemBuilder.setUnbreakable(tagCompound.getBoolean("Unbreakable"));
            }

            if (tagCompound.hasKey("HideFlags")) {
                int hideFlagsValue = tagCompound.getInteger("HideFlags");
                Set<HideFlag> hideFlags = HideFlag.VALUES.stream()
                  .filter(hideFlag -> (hideFlagsValue & hideFlag.getBit()) != 0)
                  .collect(Collectors.toSet());
                itemBuilder.setHideFlags(hideFlags);
            }

            if (tagCompound.hasKey("RepairCost")) {
                itemBuilder.setRepairCost(tagCompound.getInteger("RepairCost"));
            }

            if (tagCompound.hasKey("ench")) {
                itemBuilder.setGlowing(true);
            }

            if (tagCompound.hasKey("ench")) {
                NBTTagList enchTagList = tagCompound.getTagList("ench", 10);
                Map<Enchantment, Integer> enchantments = new HashMap<>();

                for (int i = 0; i < enchTagList.tagCount(); i++) {
                    NBTTagCompound enchTag = enchTagList.getCompoundTagAt(i);
                    Enchantment enchantment = Enchantment.byId(enchTag.getShort("id"));
                    int level = enchTag.getShort("lvl");
                    if (enchantment != null) {
                        enchantments.put(enchantment, level);
                    }
                }

                itemBuilder.setEnchantments(enchantments);
            }

            if (displayTag != null && displayTag.hasKey("color")) {
                itemBuilder.setColor(displayTag.getInteger("color"));
            }

            if (tagCompound.hasKey("SkullOwner")) {
                if (tagCompound.hasKey("SkullOwner", 8)) {
                    itemBuilder.setSkullName(tagCompound.getString("SkullOwner"));
                } else if (tagCompound.hasKey("SkullOwner", 10)) {
                    NBTTagCompound skullOwner = tagCompound.getCompoundTag("SkullOwner");
                    if (skullOwner.hasKey("Name")) {
                        itemBuilder.setSkullName(skullOwner.getString("Name"));
                    }
                    if (skullOwner.hasKey("Properties")) {
                        NBTTagCompound properties = skullOwner.getCompoundTag("Properties");
                        if (properties.hasKey("textures")) {
                            NBTTagList textures = properties.getTagList("textures", 10);
                            if (textures.tagCount() > 0) {
                                NBTTagCompound textureValue = textures.getCompoundTagAt(0);
                                if (textureValue.hasKey("Value")) {
                                    itemBuilder.setSkullValue(textureValue.getString("Value"));
                                }
                            }
                        }
                    }
                }
            }
        }

        return itemBuilder;
    }

    private ItemDataFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
