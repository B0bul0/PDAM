package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class GhastMetadata extends LivingEntityMetadata {

    // Metadata index 16
    public boolean isAttacking;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends GhastMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<GhastMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isAttacking = accessor.getBoolean(16);
        }
    }

}

