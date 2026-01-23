package me.bobulo.mine.pdam.util;

import lombok.Value;
import net.minecraft.util.BlockPos;

@Value
public class BlockPosition {

    public static BlockPosition from(BlockPos pos) {
        if (pos == null) {
            return null;
        }

        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    double x;
    double y;
    double z;

}
