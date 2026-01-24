package me.bobulo.mine.pdam.feature.particle.mapper;

/**
 * Interface for mapping particle names between different formats.
 */
public interface ParticleMapper {

    ParticleMapper VANILLA = new VanillaParticleMapper();
    ParticleMapper PARTICLE_ID = new IdParticleMapper();
    ParticleMapper CLIENT_ENUM = new EnumParticleMapper();
    ParticleMapper BUKKIT_1_8 = new Bukkit1_8ParticleMapper();

    /**
     * Maps a particle ID to its corresponding particle name.
     *
     * @param particleId the particle ID to map
     * @return the mapped particle name
     */
    String mapParticleId(int particleId);

    /**
     * Reverse maps a particle name to its corresponding particle ID.
     *
     * @param particleType the particle name to reverse map
     * @return the mapped particle ID
     */
    int reverseMapParticleId(String particleType);

}
