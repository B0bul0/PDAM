package me.bobulo.mine.pdam.feature.particle.mapper;

public class IdParticleMapper implements ParticleMapper {

    @Override
    public String mapParticleId(int particleId) {
        return String.valueOf(particleId);
    }

    @Override
    public int reverseMapParticleId(String particleId) {
        try {
            return Integer.parseInt(particleId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
