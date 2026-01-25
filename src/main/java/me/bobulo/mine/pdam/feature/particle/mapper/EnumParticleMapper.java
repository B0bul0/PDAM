package me.bobulo.mine.pdam.feature.particle.mapper;

import net.minecraft.util.EnumParticleTypes;

/**
 * EnumParticleTypes <-> particle ID mapper.
 */
public class EnumParticleMapper implements ParticleMapper {

    @Override
    public String mapParticleId(int particleId) {
        EnumParticleTypes type = EnumParticleTypes.getParticleFromId(particleId);
        return type == null ? null : type.name();
    }

    @Override
    public int reverseMapParticleId(String particleType) {
        try {
            EnumParticleTypes type = EnumParticleTypes.valueOf(particleType);
            return type.getParticleID();
        } catch (Exception e) {
            return -1;
        }
    }

}
