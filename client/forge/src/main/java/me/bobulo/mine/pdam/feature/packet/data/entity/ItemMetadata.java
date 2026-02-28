package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemMetadata extends EntityMetadata {

    // Metadata index 10
    public ItemStack item;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends ItemMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<ItemMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.item = accessor.getItemStack(10);
        }
    }

}

