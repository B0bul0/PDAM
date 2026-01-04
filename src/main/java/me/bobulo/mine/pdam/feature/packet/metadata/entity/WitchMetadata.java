package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class WitchMetadata extends LivingEntityMetadata {

    // Metadata index 21
    public boolean isAggressive;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends WitchMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<WitchMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isAggressive = accessor.getBoolean(21);
        }
    }

}

