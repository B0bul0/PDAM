package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class LivingEntityMetadata extends EntityMetadata {

    // Metadata index 6
    public float health;

    // Metadata index 7
    public int potionEffectColor;

    // Metadata index 8
    public boolean potionEffectAmbient;

    // Metadata index 9
    public byte numberOfArrows;

    // Metadata index 15
    public boolean aiDisabled;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends LivingEntityMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<LivingEntityMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.health = accessor.getFloat(6);
            metadata.potionEffectColor = accessor.getInt(7);
            metadata.potionEffectAmbient = accessor.getBoolean(8);
            metadata.numberOfArrows = accessor.getByte(9);
            metadata.aiDisabled = accessor.getBoolean(15);
        }
    }

}
