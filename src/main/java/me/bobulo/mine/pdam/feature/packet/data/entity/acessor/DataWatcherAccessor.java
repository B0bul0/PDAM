package me.bobulo.mine.pdam.feature.packet.data.entity.acessor;

import net.minecraft.entity.DataWatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotations;

/**
 * DataAccessor implementation for DataWatcher.
 */
public class DataWatcherAccessor implements DataAccessor {

    private final DataWatcher dataWatcher;

    public DataWatcherAccessor(DataWatcher dataWatcher) {
        this.dataWatcher = dataWatcher;
    }

    @Override
    public byte getByte(int index) {
        return dataWatcher.getWatchableObjectByte(index);
    }

    @Override
    public short getShort(int index) {
        return dataWatcher.getWatchableObjectShort(index);
    }

    @Override
    public boolean getBoolean(int index) {
        return dataWatcher.getWatchableObjectByte(index) != 0;
    }

    @Override
    public int getInt(int index) {
        return dataWatcher.getWatchableObjectInt(index);
    }

    @Override
    public float getFloat(int index) {
        return dataWatcher.getWatchableObjectFloat(index);
    }

    @Override
    public String getString(int index) {
        return dataWatcher.getWatchableObjectString(index);
    }

    @Override
    public ItemStack getItemStack(int index) {
        return dataWatcher.getWatchableObjectItemStack(index);
    }

    @Override
    public Rotations getRotations(int index) {
        return dataWatcher.getWatchableObjectRotations(index);
    }

}
