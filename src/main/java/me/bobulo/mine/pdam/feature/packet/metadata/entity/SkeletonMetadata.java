package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class SkeletonMetadata extends LivingEntityMetadata {

    // Metadata index 13
    public SkeletonType skeletonType;

    public enum SkeletonType {
        NORMAL(0),
        WITHER(1);

        public final int value;

        SkeletonType(int value) {
            this.value = value;
        }

        public static SkeletonType fromValue(int value) {
            for (SkeletonType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }

            return NORMAL;
        }
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends SkeletonMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<SkeletonMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.skeletonType = SkeletonType.fromValue(accessor.getByte(13));
        }
    }

}

