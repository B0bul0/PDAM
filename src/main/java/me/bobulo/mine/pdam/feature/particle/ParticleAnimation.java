package me.bobulo.mine.pdam.feature.particle;

import me.bobulo.mine.pdam.util.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

import static net.minecraft.util.EnumParticleTypes.BLOCK_DUST;

public class ParticleAnimation {

    private final Random avRandomizer = new Random();

    private Particle particle;
    private int ticksInterval;
    private int ticks;
    private boolean running = false;

    public ParticleAnimation(Particle particle, int ticksInterval) {
        this.particle = particle;
        this.ticksInterval = ticksInterval;
        this.ticks = ticksInterval;
    }

    public Particle getParticle() {
        return particle;
    }

    public boolean isRunning() {
        return running;
    }

    public int getTicksInterval() {
        return ticksInterval;
    }

    public void setTicksInterval(int ticksInterval) {
        this.ticksInterval = ticksInterval;
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;
        ticks = ticksInterval;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        ticks = 0;
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void spawnParticles() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null) return;

        handleParticles(particle);
    }

    private void handleParticles(Particle particle) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null) return;
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
//                logger.warn("Could not spawn particle effect " + particle.getType());
            }
        } else {
            for (int i = 0; i < particle.getCount(); ++i) {
                double offsetX = this.avRandomizer.nextGaussian() * (double) particle.getOffsetX();
                double offsetY = this.avRandomizer.nextGaussian() * (double) particle.getOffsetY();
                double offsetZ = this.avRandomizer.nextGaussian() * (double) particle.getOffsetZ();
                double speedX = this.avRandomizer.nextGaussian() * (double) particle.getSpeed();
                double speedY = this.avRandomizer.nextGaussian() * (double) particle.getSpeed();
                double speedZ = this.avRandomizer.nextGaussian() * (double) particle.getSpeed();

                try {
                    mc.theWorld.spawnParticle(enumParticleTypes, particle.isLongDistance(),
                      position.getX() + offsetX, position.getY() + offsetY, position.getZ() + offsetZ,
                      speedX, speedY, speedZ, extra);
                } catch (Exception e) {
//                    logger.warn("Could not spawn particle effect " + packetIn.getParticleType());
                    return;
                }
            }
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!running || event.phase != TickEvent.Phase.END) {
            return;
        }

        if (--ticks > 0) {
            return;
        }

        spawnParticles();
        ticks = ticksInterval;
    }

}
