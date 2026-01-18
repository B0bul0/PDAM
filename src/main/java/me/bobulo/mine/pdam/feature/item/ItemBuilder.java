package me.bobulo.mine.pdam.feature.item;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class ItemBuilder {

    private String material;

    private int data;
    private boolean durability;

    private int amount = 1;

    private String customName;
    private String customLore;

    private List<HideFlag> hideFlags;
    private boolean unbreakable;

    private boolean glowing;
    private Map<String, Integer> enchantments;


    // Armor color
    private Integer color;


    public ItemStack buildItem() {
        Item item = Item.getByNameOrId(material);
        ItemStack itemStack = new ItemStack(item, amount, (short) data);

        if (durability) {
            itemStack.setItemDamage(itemStack.getMaxDamage() - data);
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
            for (Map.Entry<String, Integer> enchantmentEntry : enchantments.entrySet()) {
                NBTTagCompound enchTag = new NBTTagCompound();
                enchTag.setShort("id", (short) Integer.parseInt(enchantmentEntry.getKey()));
                enchTag.setShort("lvl", (short) (int) enchantmentEntry.getValue());
                enchTagList.appendTag(enchTag);
            }
            tagCompound.setTag("ench", enchTagList);
        }

        if (color != null) {
            NBTTagCompound displayTag = tagCompound.getCompoundTag("display");
            displayTag.setInteger("color", color);
            tagCompound.setTag("display", displayTag);
        }

//        itemStack.setItemDamage(durability);


        return itemStack;
    }

    public enum HideFlag {
        ENCHANTS,
        ATTRIBUTES,
        UNBREAKABLE,
        DESTROYS,
        PLACED_ON,
        POTION_EFFECTS;

        public static final List<HideFlag> VALUES = ImmutableList.copyOf(values());

        private final int bit;

        HideFlag() {
            this.bit = 1 << this.ordinal();
        }

        public int getBit() {
            return bit;
        }
    }

}
