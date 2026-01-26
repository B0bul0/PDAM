package me.bobulo.mine.pdam.feature.particle;

import lombok.Data;
import me.bobulo.mine.pdam.util.Position;

@Data
public class Particle implements Cloneable {

    private Position position;
    private int particleId;
    private int count;
    private float speed;

    // Block data
    private int blockId;
    private int blockData;

    // Offset
    private float offsetX;
    private float offsetY;
    private float offsetZ;

    private boolean isLongDistance;

    @Override
    public Particle clone() {
        try {
            return (Particle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
