package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class TameableMetadata extends AgeableMetadata {

    // Metadata index 16 - Bit Mask
    public boolean isSitting; // 0x01
    public boolean isTame; // 0x04

    // Metadata index 17
    public String ownerName;

    public byte getStatusFlags() {
        byte flags = 0;
        if (isSitting) flags |= 0x01;
        if (isTame) flags |= 0x04;
        return flags;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends TameableMetadata> extends AgeableMetadata.Populator<T> {

        public static final Populator<TameableMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            byte flags = accessor.getByte(16);
            metadata.isSitting = (flags & 0x01) != 0;
            metadata.isTame = (flags & 0x04) != 0;
            metadata.ownerName = accessor.getString(17);
        }
    }

}

