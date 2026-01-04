package me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotations;

/**
 * Wrapper for accessing DataWatcher data from different sources.
 */
public interface DataAccessor {

    byte getByte(int index);

    short getShort(int index);

    boolean getBoolean(int index);

    int getInt(int index);

    float getFloat(int index);

    String getString(int index);

    ItemStack getItemStack(int index);

    Rotations getRotations(int index);

}