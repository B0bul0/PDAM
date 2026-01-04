package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class EndermanMetadata extends LivingEntityMetadata {

    // Metadata index 16
    public short carriedBlock;

    // Metadata index 17
    public byte carriedBlockData;

    // Metadata index 18
    public boolean isScreaming;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends EndermanMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<EndermanMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.carriedBlock = accessor.getShort(16);
            metadata.carriedBlockData = accessor.getByte(17);
            metadata.isScreaming = accessor.getBoolean(18);
        }
    }

}

