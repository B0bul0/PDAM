package me.bobulo.mine.pdam.feature.particle.mapper;

import net.minecraft.util.EnumParticleTypes;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VanillaParticleMapper implements ParticleMapper {

    private static final Map<String, EnumParticleTypes> MAPPER =
      Arrays.stream(EnumParticleTypes.values()).collect(
        Collectors.toMap(
          EnumParticleTypes::getParticleName,
          Function.identity()
        )
      );

    @Override
    public String mapParticleId(int particleId) {
        EnumParticleTypes type = EnumParticleTypes.getParticleFromId(particleId);
        return type == null ? null : type.getParticleName();
    }

    @Override
    public int reverseMapParticleId(String particleName) {
        EnumParticleTypes type = MAPPER.get(particleName);
        return type != null ? type.getParticleID() : -1;
    }

}
