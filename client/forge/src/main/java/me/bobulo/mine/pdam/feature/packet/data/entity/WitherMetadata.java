package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class WitherMetadata extends LivingEntityMetadata {

    // Metadata index 17
    public int watchedTargetId1;

    // Metadata index 18
    public int watchedTargetId2;

    // Metadata index 19
    public int watchedTargetId3;

    // Metadata index 20
    public int invulnerableTime;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends WitherMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<WitherMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.watchedTargetId1 = accessor.getInt(17);
            metadata.watchedTargetId2 = accessor.getInt(18);
            metadata.watchedTargetId3 = accessor.getInt(19);
            metadata.invulnerableTime = accessor.getInt(20);
        }
    }

}

