package me.bobulo.mine.pdam.feature.packet.data.entity.factory;

import me.bobulo.mine.pdam.feature.packet.data.entity.*;

import java.util.HashMap;
import java.util.Map;

public final class EntityMetadataRegistry {

    private static Map<Integer, Class<? extends EntityMetadata>> idToClass = new HashMap<>();

    static {
        addMetadataClass(ItemMetadata.class, 1);
        addMetadataClass(ArrowMetadata.class, 10);
        addMetadataClass(ItemFrameMetadata.class, 18);
        addMetadataClass(WitherSkullMetadata.class, 19);
        addMetadataClass(EntityMetadata.class, 20); // PrimedTnt
        addMetadataClass(FireworkMetadata.class, 22);
        addMetadataClass(ArmorStandMetadata.class, 30);
        addMetadataClass(BoatMetadata.class, 41);
        addMetadataClass(MinecartMetadata.class, 42);
        addMetadataClass(MinecartMetadata.class, 43); // ChestMinecart
        addMetadataClass(FurnaceMinecartMetadata.class, 44);
        addMetadataClass(MinecartMetadata.class, 45); // TNTMinecart
        addMetadataClass(MinecartMetadata.class, 46); // HopperMinecart
        addMetadataClass(MinecartMetadata.class, 47); // MobSpawnerMinecart
        addMetadataClass(CommandBlockMinecartMetadata.class, 40);
        addMetadataClass(LivingEntityMetadata.class, 48); // Generic Mob
        addMetadataClass(LivingEntityMetadata.class, 49); // Generic Monster
        addMetadataClass(CreeperMetadata.class, 50);
        addMetadataClass(SkeletonMetadata.class, 51);
        addMetadataClass(SpiderMetadata.class, 52);
        addMetadataClass(LivingEntityMetadata.class, 53); // Giant
        addMetadataClass(ZombieMetadata.class, 54);
        addMetadataClass(SlimeMetadata.class, 55);
        addMetadataClass(GhastMetadata.class, 56);
        addMetadataClass(ZombieMetadata.class, 57); // PigZombie
        addMetadataClass(EndermanMetadata.class, 58);
        addMetadataClass(SpiderMetadata.class, 59); // CaveSpider
        addMetadataClass(LivingEntityMetadata.class, 60); //Silverfish
        addMetadataClass(BlazeMetadata.class, 61);
        addMetadataClass(SlimeMetadata.class, 62); // MagmaCube
        addMetadataClass(LivingEntityMetadata.class, 63); //EnderDragon
        addMetadataClass(WitherMetadata.class, 64);
        addMetadataClass(BatMetadata.class, 65);
        addMetadataClass(WitchMetadata.class, 66);
        addMetadataClass(LivingEntityMetadata.class, 67); // Endermite
        addMetadataClass(GuardianMetadata.class, 68);
        addMetadataClass(PigMetadata.class, 90);
        addMetadataClass(SheepMetadata.class, 91);
        addMetadataClass(AgeableMetadata.class, 92); // Cow
        addMetadataClass(AgeableMetadata.class, 93); // Chicken
        addMetadataClass(LivingEntityMetadata.class, 94); // Squid
        addMetadataClass(WolfMetadata.class, 95);
        addMetadataClass(AgeableMetadata.class, 96); // MushroomCow
        addMetadataClass(LivingEntityMetadata.class, 97); // SnowMan
        addMetadataClass(OcelotMetadata.class, 98);
        addMetadataClass(IronGolemMetadata.class, 99);
        addMetadataClass(HorseMetadata.class, 100);
        addMetadataClass(RabbitMetadata.class, 101);
        addMetadataClass(VillagerMetadata.class, 120);
        addMetadataClass(EnderCrystalMetadata.class, 200);
    }

    public static void addMetadataClass(Class<? extends EntityMetadata> clazz, int id) {
        idToClass.put(id, clazz);
    }

    public static Class<? extends EntityMetadata> getMetadataClassById(int id) {
        return idToClass.getOrDefault(id, EntityMetadata.class);
    }

}
