package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class HorseMetadata extends AgeableMetadata {

    // Metadata index 16 - Bit Mask
    public boolean isTame; // 0x02
    public boolean hasSaddle; // 0x04
    public boolean hasChest; // 0x08
    public boolean isBred; // 0x10
    public boolean isEating; // 0x20
    public boolean isRearing; // 0x40
    public boolean mouthOpen; // 0x80

    // Metadata index 19 - Horse Type
    public HorseType horseType;

    // Metadata index 20 - Color and Style
    public HorseColor color;
    public HorseStyle style;

    // Metadata index 21
    public String ownerName;

    // Metadata index 22
    public ArmorType armorType;

    public enum HorseType {
        HORSE(0),
        DONKEY(1),
        MULE(2),
        ZOMBIE(3),
        SKELETON(4);

        public final int value;

        HorseType(int value) {
            this.value = value;
        }

        public static HorseType fromValue(int value) {
            for (HorseType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }

            return HORSE;
        }
    }

    public enum HorseColor {
        WHITE(0),
        CREAMY(1),
        CHESTNUT(2),
        BROWN(3),
        BLACK(4),
        GRAY(5),
        DARK_BROWN(6);

        public final int value;

        HorseColor(int value) {
            this.value = value;
        }

        public static HorseColor fromValue(int value) {
            for (HorseColor color : values()) {
                if (color.value == value) {
                    return color;
                }
            }

            return WHITE;
        }
    }

    public enum HorseStyle {
        NONE(0),
        WHITE(1),
        WHITEFIELD(2),
        WHITE_DOTS(3),
        BLACK_DOTS(4);

        public final int value;

        HorseStyle(int value) {
            this.value = value;
        }

        public static HorseStyle fromValue(int value) {
            for (HorseStyle style : values()) {
                if (style.value == value) {
                    return style;
                }
            }

            return NONE;
        }
    }

    public enum ArmorType {
        NO_ARMOR(0),
        IRON(1),
        GOLD(2),
        DIAMOND(3);

        public final int value;

        ArmorType(int value) {
            this.value = value;
        }

        public static ArmorType fromValue(int value) {
            for (ArmorType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }

            return NO_ARMOR;
        }
    }

    public int getStatusFlags() {
        int flags = 0;
        if (isTame) flags |= 0x02;
        if (hasSaddle) flags |= 0x04;
        if (hasChest) flags |= 0x08;
        if (isBred) flags |= 0x10;
        if (isEating) flags |= 0x20;
        if (isRearing) flags |= 0x40;
        if (mouthOpen) flags |= 0x80;
        return flags;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends HorseMetadata> extends AgeableMetadata.Populator<T> {

        public static final Populator<HorseMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            int flags = accessor.getInt(16);
            metadata.isTame = (flags & 0x02) != 0;
            metadata.hasSaddle = (flags & 0x04) != 0;
            metadata.hasChest = (flags & 0x08) != 0;
            metadata.isBred = (flags & 0x10) != 0;
            metadata.isEating = (flags & 0x20) != 0;
            metadata.isRearing = (flags & 0x40) != 0;
            metadata.mouthOpen = (flags & 0x80) != 0;

            metadata.horseType = HorseType.fromValue(accessor.getByte(19));

            int variant = accessor.getInt(20);
            metadata.color = HorseColor.fromValue(variant & 0xFF);
            metadata.style = HorseStyle.fromValue((variant >> 8) & 0xFF);

            metadata.ownerName = accessor.getString(21);
            metadata.armorType = ArmorType.fromValue(accessor.getInt(22));
        }
    }

}

