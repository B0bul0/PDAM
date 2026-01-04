package me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor;

import net.minecraft.entity.DataWatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotations;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataAccessor implementation for List of WatchableObjects.
 */
public class WatchableListAccessor implements DataAccessor {

    private final Map<Integer, DataWatcher.WatchableObject> objectMap;

    public WatchableListAccessor(List<DataWatcher.WatchableObject> watchableObjects) {
        this.objectMap = new HashMap<>();
        for (DataWatcher.WatchableObject obj : watchableObjects) {
            objectMap.put(obj.getDataValueId(), obj);
        }
    }

    @Nullable
    private Object getObject(int index) {
        DataWatcher.WatchableObject obj = objectMap.get(index);
        return obj != null ? obj.getObject() : null;
    }

    @Override
    public byte getByte(int index) {
        Object obj = getObject(index);
        return obj instanceof Byte ? (Byte) obj : 0;
    }

    @Override
    public short getShort(int index) {
        Object obj = getObject(index);
        return obj instanceof Short ? (Short) obj : 0;
    }

    @Override
    public boolean getBoolean(int index) {
        return getByte(index) != 0;
    }

    @Override
    public int getInt(int index) {
        Object obj = getObject(index);
        return obj instanceof Integer ? (Integer) obj : 0;
    }

    @Override
    public float getFloat(int index) {
        Object obj = getObject(index);
        return obj instanceof Float ? (Float) obj : 0.0f;
    }

    @Override
    public String getString(int index) {
        Object obj = getObject(index);
        return obj instanceof String ? (String) obj : "";
    }

    @Override
    public ItemStack getItemStack(int index) {
        Object obj = getObject(index);
        return obj instanceof ItemStack ? (ItemStack) obj : null;
    }

    @Override
    public Rotations getRotations(int index) {
        Object obj = getObject(index);
        return obj instanceof Rotations ? (Rotations) obj : new Rotations(0, 0, 0);
    }

}