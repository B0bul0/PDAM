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

    @SuppressWarnings("unchecked")
    public static NBTTagCompound toNBT(Map<String, Object> map) {
        NBTTagCompound compound = new NBTTagCompound();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Byte) {
                compound.setByte(key, (Byte) value);
            } else if (value instanceof Short) {
                compound.setShort(key, (Short) value);
            } else if (value instanceof Integer) {
                compound.setInteger(key, (Integer) value);
            } else if (value instanceof Long) {
                compound.setLong(key, (Long) value);
            } else if (value instanceof Float) {
                compound.setFloat(key, (Float) value);
            } else if (value instanceof Double) {
                compound.setDouble(key, (Double) value);
            } else if (value instanceof String) {
                compound.setString(key, (String) value);
            } else if (value instanceof byte[]) { // Adicionado
                compound.setByteArray(key, (byte[]) value);
            } else if (value instanceof int[]) { // Adicionado
                compound.setIntArray(key, (int[]) value);
            } else if (value instanceof Map) {
                compound.setTag(key, toNBT((Map<String, Object>) value));
            } else if (value instanceof List) {
                compound.setTag(key, toNBTList((List<Object>) value));
            } else if (value instanceof Boolean) {
                compound.setByte(key, (byte) ((Boolean) value ? 1 : 0));
            }
        }

        return compound;
    }

    @SuppressWarnings("unchecked")
    private static NBTTagList toNBTList(List<Object> list) {
        NBTTagList nbtList = new NBTTagList();
        for (Object item : list) {
            if (item instanceof Map) {
                nbtList.appendTag(toNBT((Map<String, Object>) item));
            } else if (item instanceof String) {
                nbtList.appendTag(new NBTTagString((String) item));
            } else if (item instanceof Float) {
                nbtList.appendTag(new NBTTagFloat((Float) item));
            } else if (item instanceof Double) {
                nbtList.appendTag(new NBTTagDouble((Double) item));
            } else if (item instanceof Integer) {
                nbtList.appendTag(new NBTTagInt((Integer) item));
            } else if (item instanceof Byte) {
                nbtList.appendTag(new NBTTagByte((Byte) item));
            } else if (item instanceof Short) {
                nbtList.appendTag(new NBTTagShort((Short) item));
            } else if (item instanceof Long) {
                nbtList.appendTag(new NBTTagLong((Long) item));
            } else if (item instanceof byte[]) {
                nbtList.appendTag(new NBTTagByteArray((byte[]) item));
            } else if (item instanceof int[]) {
                nbtList.appendTag(new NBTTagIntArray((int[]) item));
            } else if (item instanceof List) {
                nbtList.appendTag(toNBTList((List<Object>) item));
            }
        }
        return nbtList;
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

