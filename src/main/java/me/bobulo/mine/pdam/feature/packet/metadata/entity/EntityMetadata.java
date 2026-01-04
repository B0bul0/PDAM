package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import me.bobulo.mine.pdam.feature.packet.metadata.entity.populator.MetadataPopulator;
import org.jetbrains.annotations.NotNull;

public class EntityMetadata {

    // Metadata index 0
    public boolean onFire; // 0x01
    public boolean crouched; // 0x02
    public boolean sprinting; // 0x08
    public boolean eating; // 0x10
    public boolean invisible; // 0x20

    // Metadata index 1
    public short airTicks;

    // Metadata index 2
    public String nameTag;

    // Metadata index 3
    public boolean alwaysShowNameTag;

    // Metadata index 4
    public byte silent;

    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends EntityMetadata> implements MetadataPopulator<T> {

        public static final Populator<EntityMetadata> INSTANCE = new Populator<>();

        protected Populator() {}

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            byte meta0 = accessor.getByte(0);
            metadata.onFire = (meta0 & 0x01) != 0;
            metadata.crouched = (meta0 & 0x02) != 0;
            metadata.sprinting = (meta0 & 0x08) != 0;
            metadata.eating = (meta0 & 0x10) != 0;
            metadata.invisible = (meta0 & 0x20) != 0;

            metadata.airTicks = accessor.getShort(1);
            metadata.nameTag = accessor.getString(2);
            metadata.alwaysShowNameTag = accessor.getBoolean(3);
            metadata.silent = accessor.getByte(4);
        }
    }

}
