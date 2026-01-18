package me.bobulo.mine.pdam.feature.item;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ItemBuilder {

    public static ItemBuilder fromItemStack(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder();

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
                List<HideFlag> hideFlags = HideFlag.VALUES.stream()
                        .filter(hideFlag -> (hideFlagsValue & hideFlag.getBit()) != 0)
                        .collect(Collectors.toList());
                itemBuilder.setHideFlags(hideFlags);
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
                NBTTagCompound skullOwner = tagCompound.getCompoundTag("SkullOwner");
                if (skullOwner != null && skullOwner.hasKey("Properties")) {
                    NBTTagCompound properties = skullOwner.getCompoundTag("Properties");
                    if (properties != null && properties.hasKey("textures")) {
                        NBTTagList textures = properties.getTagList("textures", 10);
                        if (textures.tagCount() > 0) {
                            NBTTagCompound textureValue = textures.getCompoundTagAt(0);
                            if (textureValue != null && textureValue.hasKey("Value")) {
                                itemBuilder.setSkullValue(textureValue.getString("Value"));
                            }
                        }
                    }
                }
            }
        }

        return itemBuilder;
    }

    private String material;

    private int durability;

    private int amount = 1;

    private String customName;
    private String customLore;

    private List<HideFlag> hideFlags;
    private boolean unbreakable;

    private boolean glowing;
    private Map<Enchantment, Integer> enchantments;

    private int repairCost; // todo

    // Armor color
    private Integer color;

    // Skull
    private String skullValue;


    public ItemStack buildItem() {
        ItemStack itemStack = ItemStackResolver.resolve(material);
        itemStack.stackSize = amount;

        if (durability > 0) {
            itemStack.setItemDamage(itemStack.getMaxDamage() - durability);
        }

        if (StringUtils.isNoneBlank(customName)) {
            itemStack.setStackDisplayName(customName);
        }

        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            itemStack.setTagCompound(tagCompound);
        }

        if (StringUtils.isNoneBlank(customLore)) {
            NBTTagCompound displayTag = tagCompound.getCompoundTag("display");
            NBTTagList loreTag = new NBTTagList();
            for (String line : customLore.split("\n")) {
                loreTag.appendTag(new NBTTagString(line));
            }
            displayTag.setTag("Lore", loreTag);
            tagCompound.setTag("display", displayTag);
        }

        if (unbreakable) {
            tagCompound.setBoolean("Unbreakable", true);
        }

        if (hideFlags != null && !hideFlags.isEmpty()) {
            int hideFlagsValue = 0;
            for (HideFlag hideFlag : hideFlags) {
                hideFlagsValue |= hideFlag.getBit();
            }
            tagCompound.setInteger("HideFlags", hideFlagsValue);
        }

        if (glowing) {
            tagCompound.setTag("ench", new NBTTagCompound());
        }

        if (enchantments != null && !enchantments.isEmpty()) {
            NBTTagList enchTagList = new NBTTagList();
            for (Map.Entry<Enchantment, Integer> enchantmentEntry : enchantments.entrySet()) {
                if (enchantmentEntry.getKey() == null || enchantmentEntry.getValue() == null) {
                    continue;
                }

                NBTTagCompound enchTag = new NBTTagCompound();
                enchTag.setShort("id", (short) enchantmentEntry.getKey().getId());
                enchTag.setShort("lvl", enchantmentEntry.getValue().shortValue());
                enchTagList.appendTag(enchTag);
            }
            tagCompound.setTag("ench", enchTagList);
        }

        if (color != null) {
            NBTTagCompound displayTag = tagCompound.getCompoundTag("display");
            displayTag.setInteger("color", color);
            tagCompound.setTag("display", displayTag);
        }

        if (StringUtils.isNoneBlank(skullValue)) {
            NBTTagCompound skullOwner = new NBTTagCompound();
            skullOwner.setString("Id", UUID.randomUUID().toString());
            skullOwner.setString("Name", "Custom");

            NBTTagCompound properties = new NBTTagCompound();
            NBTTagList textures = new NBTTagList();
            NBTTagCompound textureValue = new NBTTagCompound();
            textureValue.setString("Value", skullValue);

            textures.appendTag(textureValue);
            properties.setTag("textures", textures);
            skullOwner.setTag("Properties", properties);
            tagCompound.setTag("SkullOwner", skullOwner);
        }

        return itemStack;
    }

}
