package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class ZombieMetadata extends LivingEntityMetadata {

    // Metadata index 12
    public boolean isChild;

    // Metadata index 13
    public boolean isVillager;

    // Metadata index 14
    public boolean isConverting;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends ZombieMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<ZombieMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isChild = accessor.getBoolean(12);
            metadata.isVillager = accessor.getBoolean(13);
            metadata.isConverting = accessor.getBoolean(14);
        }
    }

}

