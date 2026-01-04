package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class WolfMetadata extends TameableMetadata {

    // Metadata index 16 - Additional flag
    public boolean isAngry; // 0x02

    // Metadata index 18
    public float wolfHealth;

    // Metadata index 19
    public byte begging;

    // Metadata index 20
    public byte collarColor;

    @Override
    public byte getStatusFlags() {
        byte flags = super.getStatusFlags();
        if (isAngry) flags |= 0x02;
        return flags;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends WolfMetadata> extends TameableMetadata.Populator<T> {

        public static final Populator<WolfMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            byte flags = accessor.getByte(16);
            metadata.isAngry = (flags & 0x02) != 0;
            metadata.wolfHealth = accessor.getFloat(18);
            metadata.begging = accessor.getByte(19);
            metadata.collarColor = accessor.getByte(20);
        }
    }

}

