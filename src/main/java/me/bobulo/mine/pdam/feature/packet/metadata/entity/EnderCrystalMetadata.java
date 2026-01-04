package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class EnderCrystalMetadata extends EntityMetadata {

    // Metadata index 8
    public int health;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends EnderCrystalMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<EnderCrystalMetadata> INSTANCE = new Populator<>();

        protected Populator() {

        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.health = accessor.getInt(8);
        }
    }

}

