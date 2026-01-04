package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class FurnaceMinecartMetadata extends MinecartMetadata {

    // Metadata index 16
    public boolean isPowered;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends FurnaceMinecartMetadata> extends MinecartMetadata.Populator<T> {

        public static final Populator<FurnaceMinecartMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.isPowered = accessor.getBoolean(16);
        }
    }

}

