package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class SheepMetadata extends AgeableMetadata {

    // Metadata index 16
    public WoolColor woolColor;
    public boolean isSheared; // 0x10

    public enum WoolColor {
        WHITE(0), ORANGE(1), MAGENTA(2), LIGHT_BLUE(3),
        YELLOW(4), LIME(5), PINK(6), GRAY(7),
        SILVER(8), CYAN(9), PURPLE(10), BLUE(11),
        BROWN(12), GREEN(13), RED(14), BLACK(15);

        public final int value;

        WoolColor(int value) {
            this.value = value;
        }

        public static WoolColor fromValue(int value) {
            for (WoolColor color : values()) {
                if (color.value == value) {
                    return color;
                }
            }

            return WHITE;
        }
    }

    public byte getWoolData() {
        byte data = (byte) woolColor.value;
        if (isSheared) data |= 0x10;
        return data;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends SheepMetadata> extends AgeableMetadata.Populator<T> {

        public static final Populator<SheepMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            byte data = accessor.getByte(16);
            metadata.woolColor = WoolColor.fromValue(data & 0x0F);
            metadata.isSheared = (data & 0x10) != 0;
        }
    }

}

