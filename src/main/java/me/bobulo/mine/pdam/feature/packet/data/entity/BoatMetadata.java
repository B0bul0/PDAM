package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class BoatMetadata extends EntityMetadata {

    // Metadata index 17
    public int timeSinceHit;

    // Metadata index 18
    public int forwardDirection;

    // Metadata index 19
    public float damageTaken;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends BoatMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<BoatMetadata> INSTANCE = new Populator<>();

        protected Populator() {

        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.timeSinceHit = accessor.getInt(17);
            metadata.forwardDirection = accessor.getInt(18);
            metadata.damageTaken = accessor.getFloat(19);
        }
    }

}

