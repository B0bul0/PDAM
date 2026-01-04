package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class GuardianMetadata extends LivingEntityMetadata {

    // Metadata index 16 - Bit Mask
    public boolean isElderly; // 0x02
    public boolean isRetractingSpikes; // 0x04

    // Metadata index 17
    public int targetEntityId;

    public byte getStatusFlags() {
        byte flags = 0;
        if (isElderly) flags |= 0x02;
        if (isRetractingSpikes) flags |= 0x04;
        return flags;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends GuardianMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<GuardianMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            byte flags = accessor.getByte(16);
            metadata.isElderly = (flags & 0x02) != 0;
            metadata.isRetractingSpikes = (flags & 0x04) != 0;
            metadata.targetEntityId = accessor.getInt(17);
        }
    }

}

