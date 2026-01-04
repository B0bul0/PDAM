package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class VillagerMetadata extends AgeableMetadata {

    // Metadata index 16
    public VillagerProfession profession;

    public enum VillagerProfession {
        FARMER(0),
        LIBRARIAN(1),
        PRIEST(2),
        BLACKSMITH(3),
        BUTCHER(4);

        public final int value;

        VillagerProfession(int value) {
            this.value = value;
        }

        public static VillagerProfession fromValue(int value) {
            for (VillagerProfession profession : values()) {
                if (profession.value == value) {
                    return profession;
                }
            }

            return null;
        }
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends VillagerMetadata> extends AgeableMetadata.Populator<T> {

        public static final Populator<VillagerMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.profession = VillagerProfession.fromValue(accessor.getInt(16));
        }
    }

}

