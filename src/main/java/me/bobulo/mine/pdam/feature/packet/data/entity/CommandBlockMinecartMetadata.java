package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class CommandBlockMinecartMetadata extends MinecartMetadata {

    // Metadata index 23
    public String command;

    // Metadata index 24
    public String lastOutput;

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends CommandBlockMinecartMetadata> extends MinecartMetadata.Populator<T> {

        public static final Populator<CommandBlockMinecartMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);
            metadata.command = accessor.getString(23);
            metadata.lastOutput = accessor.getString(24);
        }
    }

}