package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class WitherSkullMetadata extends LivingEntityMetadata {

    // Metadata index 10
    public boolean invulnerable;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends WitherSkullMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<WitherSkullMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.invulnerable = accessor.getBoolean(10);
        }
    }

}