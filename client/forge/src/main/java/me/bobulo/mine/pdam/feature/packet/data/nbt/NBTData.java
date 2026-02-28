package me.bobulo.mine.pdam.feature.packet.data.nbt;

import net.minecraft.nbt.*;

import java.util.*;

public class NBTData {

    private NBTData() {
        // Utility class
    }

    public static Map<String, Object> from(NBTTagCompound nbtTag) {
        if (nbtTag == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new LinkedHashMap<>();

        for (String key : nbtTag.getKeySet()) {
            NBTBase base = nbtTag.getTag(key);
            map.put(key, convertNBT(base));
        }

        return map;
    }

    private static Object convertNBT(NBTBase base) {
        if (base == null) {
            return null;
        }

        switch (base.getId()) {
            case 1: // BYTE
                return ((NBTTagByte) base).getByte();
            case 2: // SHORT
                return ((NBTTagShort) base).getShort();
            case 3: // INT
                return ((NBTTagInt) base).getInt();
            case 4: // LONG
                return ((NBTTagLong) base).getLong();
            case 5: // FLOAT
                return ((NBTTagFloat) base).getFloat();
            case 6: // DOUBLE
                return ((NBTTagDouble) base).getDouble();
            case 7: // BYTE_ARRAY
                return Base64.getEncoder().encodeToString(((NBTTagByteArray) base).getByteArray());
            case 8: // STRING
                return ((NBTTagString) base).getString();
            case 9: // LIST
                return convertNBTList((NBTTagList) base);
            case 10: // COMPOUND
                return from((NBTTagCompound) base);
            case 11: // INT_ARRAY
                return ((NBTTagIntArray) base).getIntArray();
            default:
                return base.toString();
        }
    }

    private static List<Object> convertNBTList(NBTTagList list) {
        List<Object> result = new ArrayList<>();

        for (int i = 0; i < list.tagCount(); i++) {
            int type = list.getTagType();

            switch (type) {
                case 10: // COMPOUND
                    result.add(from(list.getCompoundTagAt(i)));
                    break;
                case 8: // STRING
                    result.add(list.getStringTagAt(i));
                    break;
                case 5: // FLOAT
                    result.add(list.getFloatAt(i));
                    break;
                case 6: // DOUBLE
                    result.add(list.getDoubleAt(i));
                    break;
                case 3: // INT
                    NBTBase intBase = list.get(i);
                    if (intBase instanceof NBTTagInt) {
                        result.add(((NBTTagInt) intBase).getInt());
                    }
                    break;
                default:
                    NBTBase base = list.get(i);
                    if (base != null) {
                        result.add(convertNBT(base));
                    }
                    break;
            }
        }

        return result;
    }
}

