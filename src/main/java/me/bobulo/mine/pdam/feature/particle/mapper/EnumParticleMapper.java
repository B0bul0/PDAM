package me.bobulo.mine.pdam.feature.particle.mapper;

import net.minecraft.util.EnumParticleTypes;

public class EnumParticleMapper implements ParticleMapper {

    @Override
    public String mapParticleId(int particleName) {
        EnumParticleTypes type = EnumParticleTypes.getParticleFromId(particleName);
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
