package me.bobulo.mine.pdam.feature.particle.mapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Bukkit 1.8 Particle Mapper
 * org.bukkit.Effect
 */
public class Bukkit1_8ParticleMapper implements ParticleMapper {

    private static final BiMap<String, Integer> MAPPER = HashBiMap.create();

    static {
        register("EXPLOSION", 0);
        register("EXPLOSION_LARGE", 1);
        register("EXPLOSION_HUGE", 2);
        register("FIREWORKS_SPARK", 3);
        register("SPLASH", 5);
        register("VOID_FOG", 8);
        register("CRIT", 9);
        register("MAGIC_CRIT", 10);
        register("PARTICLE_SMOKE", 11);
        register("LARGE_SMOKE", 12);
        register("SPELL", 13);
        register("INSTANT_SPELL", 14);
        register("POTION_SWIRL", 15);
        register("POTION_SWIRL_TRANSPARENT", 16);
        register("WITCH_MAGIC", 17);
        register("WATERDRIP", 18);
        register("LAVADRIP", 19);
        register("VILLAGER_THUNDERCLOUD", 20);
        register("HAPPY_VILLAGER", 21);
        register("SMALL_SMOKE", 22);
        register("NOTE", 23);
        register("PORTAL", 24);
        register("FLYING_GLYPH", 25);
        register("FLAME", 26);
        register("LAVA_POP", 27);
        register("FOOTSTEP", 28);
        register("CLOUD", 29);
        register("COLOURED_DUST", 30);
        register("SNOWBALL_BREAK", 31);
        register("SNOW_SHOVEL", 32);
        register("SLIME", 33);
        register("HEART", 34);
        register("ITEM_BREAK", 36);
        register("TILE_BREAK", 37);
        register("TILE_DUST", 38);
    }

    private static void register(String name, int id) {
        MAPPER.put(name, id);
    }

    @Override
    public String mapParticleId(int particleName) {
        return MAPPER.inverse().get(particleName);
    }

    @Override
    public int reverseMapParticleId(String bukkitParticleName) {
        return MAPPER.getOrDefault(bukkitParticleName, -1);
    }

}
