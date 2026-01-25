package me.bobulo.mine.pdam.util;

import me.bobulo.mine.pdam.feature.particle.Particle;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class ParticleUtils {

    private static final Random RANDOM = new Random();

    private static final Logger log = LogManager.getLogger(ParticleUtils.class);

    public static void spawnParticle(@NotNull Particle particle) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null) {
            return;
        }

        EnumParticleTypes enumParticleTypes = EnumParticleTypes.getParticleFromId(particle.getParticleId());
        Position position = particle.getPosition();

        int[] extra;

        if (enumParticleTypes == EnumParticleTypes.ITEM_CRACK) {
            extra = new int[]{particle.getBlockId()};
        } else if (enumParticleTypes == EnumParticleTypes.BLOCK_CRACK || enumParticleTypes == EnumParticleTypes.BLOCK_DUST ){
            extra = new int[]{particle.getBlockData() << 12 | particle.getBlockId() & 4095};
        } else {
            extra = new int[0];
        }

        if (particle.getCount() == 0) {
            double speedX = particle.getSpeed() * particle.getOffsetX();
            double speedY = particle.getSpeed() * particle.getOffsetY();
            double speedZ = particle.getSpeed() * particle.getOffsetZ();

            try {
                mc.theWorld.spawnParticle(enumParticleTypes, particle.isLongDistance(), position.getX(), position.getY(), position.getZ(),
                  speedX, speedY, speedZ, extra);
            } catch (Exception e) {
                log.warn("Could not spawn particle effect " + particle.getParticleId());
            }
        } else {
            for (int i = 0; i < particle.getCount(); ++i) {
                double offsetX = RANDOM.nextGaussian() * (double) particle.getOffsetX();
                double offsetY = RANDOM.nextGaussian() * (double) particle.getOffsetY();
                double offsetZ = RANDOM.nextGaussian() * (double) particle.getOffsetZ();
                double speedX = RANDOM.nextGaussian() * (double) particle.getSpeed();
                double speedY = RANDOM.nextGaussian() * (double) particle.getSpeed();
                double speedZ = RANDOM.nextGaussian() * (double) particle.getSpeed();

                try {
                    mc.theWorld.spawnParticle(enumParticleTypes, particle.isLongDistance(),
                      position.getX() + offsetX, position.getY() + offsetY, position.getZ() + offsetZ,
                      speedX, speedY, speedZ, extra);
                } catch (Exception e) {
                    log.warn("Could not spawn particle effect " + particle.getParticleId());
                }
            }
        }
    }

    private ParticleUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
