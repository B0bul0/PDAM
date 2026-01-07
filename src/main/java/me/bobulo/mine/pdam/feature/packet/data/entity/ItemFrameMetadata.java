package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemFrameMetadata extends EntityMetadata {

    // Metadata index 8
    public ItemStack item;

    // Metadata index 9
    public byte rotation;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends ItemFrameMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<ItemFrameMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.item = accessor.getItemStack(8);
            metadata.rotation = accessor.getByte(9);
        }
    }

}

