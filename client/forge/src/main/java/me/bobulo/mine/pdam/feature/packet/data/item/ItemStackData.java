package me.bobulo.mine.pdam.feature.packet.data.item;

import me.bobulo.mine.pdam.feature.packet.data.nbt.NBTData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class ItemStackData {

    public int itemId;
    public int count;
    public int damage;
    public String displayName;
    public Map<String, Object> nbt;

    public static ItemStackData from(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemStackData data = new ItemStackData();
        data.itemId = Item.getIdFromItem(itemStack.getItem());
        data.count = itemStack.stackSize;
        data.damage = itemStack.getItemDamage();
        data.displayName = itemStack.getDisplayName();

        NBTTagCompound nbtTag = itemStack.getTagCompound();
        if (nbtTag != null) {
            data.nbt = NBTData.from(nbtTag);
        }

        return data;
    }
}

