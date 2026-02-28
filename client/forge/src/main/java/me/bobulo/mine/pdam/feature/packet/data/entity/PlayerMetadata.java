package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

public class PlayerMetadata extends LivingEntityMetadata {

    // Metadata index 10 - Skin flags
    public boolean capeEnabled; // 0x01
    public boolean jacketEnabled; // 0x02
    public boolean leftSleeveEnabled; // 0x04
    public boolean rightSleeveEnabled; // 0x08
    public boolean leftPantsEnabled; // 0x10
    public boolean rightPantsEnabled; // 0x20
    public boolean hatEnabled; // 0x40

    // Metadata index 16
    public byte unused;

    // Metadata index 17
    public float absorptionHearts;

    // Metadata index 18
    public int score;

    public byte getSkinFlags() {
        byte flags = 0;
        if (capeEnabled) flags |= 0x01;
        if (jacketEnabled) flags |= 0x02;
        if (leftSleeveEnabled) flags |= 0x04;
        if (rightSleeveEnabled) flags |= 0x08;
        if (leftPantsEnabled) flags |= 0x10;
        if (rightPantsEnabled) flags |= 0x20;
        if (hatEnabled) flags |= 0x40;
        return flags;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends PlayerMetadata> extends LivingEntityMetadata.Populator<T> {

        public static final Populator<PlayerMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            byte skinFlags = accessor.getByte(10);
            metadata.capeEnabled = (skinFlags & 0x01) != 0;
            metadata.jacketEnabled = (skinFlags & 0x02) != 0;
            metadata.leftSleeveEnabled = (skinFlags & 0x04) != 0;
            metadata.rightSleeveEnabled = (skinFlags & 0x08) != 0;
            metadata.leftPantsEnabled = (skinFlags & 0x10) != 0;
            metadata.rightPantsEnabled = (skinFlags & 0x20) != 0;
            metadata.hatEnabled = (skinFlags & 0x40) != 0;

            metadata.unused = accessor.getByte(16);
            metadata.absorptionHearts = accessor.getFloat(17);
            metadata.score = accessor.getInt(18);
        }
    }

}
