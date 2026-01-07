package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class SpiderMetadata extends LivingEntityMetadata {

    // Metadata index 16
    public boolean isClimbing;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends SpiderMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<SpiderMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isClimbing = accessor.getBoolean(16);
        }
    }

}

