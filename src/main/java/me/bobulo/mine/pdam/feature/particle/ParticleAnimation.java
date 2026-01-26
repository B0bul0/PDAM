package me.bobulo.mine.pdam.feature.particle;

import me.bobulo.mine.pdam.util.ParticleUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ParticleAnimation {

    private final Particle particle;
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
        ParticleUtils.spawnParticle(particle);
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
