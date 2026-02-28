package me.bobulo.mine.pdam.feature.packet.data.entity;

import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import net.minecraft.util.Rotations;
import org.jetbrains.annotations.NotNull;

public class ArmorStandMetadata extends EntityMetadata {

    // Metadata index 6
    public float health;

    // Metadata index 7
    public int potionEffectColor;

    // Metadata index 8
    public boolean potionEffectAmbient;

    // Metadata index 9
    public byte numberOfArrows;

    // Metadata index 10 - Bit Mask
    public boolean isSmall; // 0x01
    public boolean hasGravity; // 0x02
    public boolean hasArms; // 0x04
    public boolean removeBasePlate; // 0x08
    public boolean isMarker; // 0x10

    // Metadata index 11 - Head position (pitch, yaw, roll)
    public float headPitch;
    public float headYaw;
    public float headRoll;

    // Metadata index 12 - Body position
    public float bodyPitch;
    public float bodyYaw;
    public float bodyRoll;

    // Metadata index 13 - Left Arm position
    public float leftArmPitch;
    public float leftArmYaw;
    public float leftArmRoll;

    // Metadata index 14 - Right Arm position
    public float rightArmPitch;
    public float rightArmYaw;
    public float rightArmRoll;

    // Metadata index 15 - Left Leg position
    public float leftLegPitch;
    public float leftLegYaw;
    public float leftLegRoll;

    // Metadata index 16 - Right Leg position
    public float rightLegPitch;
    public float rightLegYaw;
    public float rightLegRoll;

    public byte getStatusFlags() {
        byte flags = 0;
        if (isSmall) flags |= 0x01;
        if (hasGravity) flags |= 0x02;
        if (hasArms) flags |= 0x04;
        if (removeBasePlate) flags |= 0x08;
        if (isMarker) flags |= 0x10;
        return flags;
    }

    @Override
    public void populate(DataAccessor accessor) {
        Populator.INSTANCE.populate(this, accessor);
    }

    public static class Populator<T extends ArmorStandMetadata> extends EntityMetadata.Populator<T> {

        public static final Populator<ArmorStandMetadata> INSTANCE = new Populator<>();

        protected Populator() {
        }

        @Override
        public void populate(@NotNull T metadata, @NotNull DataAccessor accessor) {
            super.populate(metadata, accessor);

            metadata.health = accessor.getFloat(6);
            metadata.potionEffectColor = accessor.getInt(7);
            metadata.potionEffectAmbient = accessor.getBoolean(8);
            metadata.numberOfArrows = accessor.getByte(9);

            byte flags = accessor.getByte(10);
            metadata.isSmall = (flags & 0x01) != 0;
            metadata.hasGravity = (flags & 0x02) != 0;
            metadata.hasArms = (flags & 0x04) != 0;
            metadata.removeBasePlate = (flags & 0x08) != 0;
            metadata.isMarker = (flags & 0x10) != 0;

            Rotations headRotation = accessor.getRotations(11);
            metadata.headPitch = headRotation.getX();
            metadata.headYaw = headRotation.getY();
            metadata.headRoll = headRotation.getZ();

            Rotations bodyRotation = accessor.getRotations(12);
            metadata.bodyPitch = bodyRotation.getX();
            metadata.bodyYaw = bodyRotation.getY();
            metadata.bodyRoll = bodyRotation.getZ();

            Rotations leftArmRotation = accessor.getRotations(13);
            metadata.leftArmPitch = leftArmRotation.getX();
            metadata.leftArmYaw = leftArmRotation.getY();
            metadata.leftArmRoll = leftArmRotation.getZ();

            Rotations rightArmRotation = accessor.getRotations(14);
            metadata.rightArmPitch = rightArmRotation.getX();
            metadata.rightArmYaw = rightArmRotation.getY();
            metadata.rightArmRoll = rightArmRotation.getZ();

            Rotations leftLegRotation = accessor.getRotations(15);
            metadata.leftLegPitch = leftLegRotation.getX();
            metadata.leftLegYaw = leftLegRotation.getY();
            metadata.leftLegRoll = leftLegRotation.getZ();

            Rotations rightLegRotation = accessor.getRotations(16);
            metadata.rightLegPitch = rightLegRotation.getX();
            metadata.rightLegYaw = rightLegRotation.getY();
            metadata.rightLegRoll = rightLegRotation.getZ();
        }
    }

}

