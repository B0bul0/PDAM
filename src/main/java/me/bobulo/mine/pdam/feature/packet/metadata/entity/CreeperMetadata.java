package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class CreeperMetadata extends LivingEntityMetadata {

    // Metadata index 16 - State (-1 = Idle, 1 = Fuse)
    public byte state;

    // Metadata index 17
    public boolean isPowered;

    public boolean isIdle() {
        return state == -1;
    }

    public boolean isFusing() {
        return state == 1;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends CreeperMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<CreeperMetadata> INSTANCE = new Populator<>();

        protected Populator() {

        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.state = accessor.getByte(16);
            metadata.isPowered = accessor.getBoolean(17);
        }
    }

}

