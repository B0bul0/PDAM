package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FireworkMetadata extends EntityMetadata {

    // Metadata index 8
    public ItemStack fireworkInfo;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends FireworkMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<FireworkMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.fireworkInfo = accessor.getItemStack(8);
        }
    }

}

