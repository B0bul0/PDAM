package me.bobulo.mine.pdam.feature.packet.metadata.entity;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class MinecartMetadata extends EntityMetadata {

    // Metadata index 17
    public int shakingPower;

    // Metadata index 18
    public int shakingDirection;

    // Metadata index 19
    public float damageTaken;

    // Metadata index 20 - Bit Mask
    public int blockId; // 0x00FF
    public int blockData; // 0xFF00

    // Metadata index 21
    public int blockYPosition;

    // Metadata index 22
    public boolean showBlock;

    public int getBlockInfo() {
        return (blockId & 0xFF) | ((blockData & 0xFF) << 8);
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends MinecartMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<MinecartMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.shakingPower = accessor.getInt(17);
            metadata.shakingDirection = accessor.getInt(18);
            metadata.damageTaken = accessor.getFloat(19);

            int blockInfo = accessor.getInt(20);
            metadata.blockId = blockInfo & 0xFF;
            metadata.blockData = (blockInfo >> 8) & 0xFF;

            metadata.blockYPosition = accessor.getInt(21);
            metadata.showBlock = accessor.getBoolean(22);
        }
    }
}

