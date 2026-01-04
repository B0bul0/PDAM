package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class RabbitMetadata extends AgeableMetadata {

    // Metadata index 18
    public byte rabbitType;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends RabbitMetadata> extends AgeableMetadata.Populator<T> {

        public static final Populator<RabbitMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.rabbitType = accessor.getByte(18);
        }
    }

}

