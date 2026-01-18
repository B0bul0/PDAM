package me.bobulo.mine.pdam.feature.designtools.item;

import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Data
public class ItemData {

    private String material;

    private int durability;

    private int amount = 1;

    private String customName;
    private String customLore;

    private Set<HideFlag> hideFlags = new HashSet<>();
    private boolean unbreakable;
    private int repairCost;

    private boolean glowing;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    // Armor color
    private Integer color;

    // Skull
    private String skullName;
    private String skullValue;

    public ItemStack buildItem() {
        ItemStack itemStack = ItemStackResolver.resolve(material);
        if (itemStack == null) {
            return null;
        }

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

        if (repairCost > 0) {
            tagCompound.setInteger("RepairCost", repairCost);
        }

        if (StringUtils.isNoneBlank(skullValue)) {
            NBTTagCompound skullOwner = new NBTTagCompound();
            skullOwner.setString("Id", UUID.randomUUID().toString());
            skullOwner.setString("Name", skullName == null ? "Custom" : skullName);

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
