package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class IronGolemMetadata extends LivingEntityMetadata {

    // Metadata index 16
    public boolean isPlayerCreated;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends IronGolemMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<IronGolemMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isPlayerCreated = accessor.getBoolean(16);
        }
    }

}

