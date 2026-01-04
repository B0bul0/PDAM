package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class AgeableMetadata extends LivingEntityMetadata {

    public byte age; // Metadata index 12

    public boolean isBaby() {
        return age < 0;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends AgeableMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<AgeableMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.age = accessor.getByte(12);
        }
    }

}
