package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class ArrowMetadata extends EntityMetadata {

    // Metadata index 16
    public boolean isCritical;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends ArrowMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<ArrowMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isCritical = accessor.getBoolean(16);
        }
    }

}

