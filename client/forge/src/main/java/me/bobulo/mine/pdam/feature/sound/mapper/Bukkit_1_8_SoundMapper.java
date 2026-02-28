package me.bobulo.mine.pdam.feature.sound.mapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Bukkit_1_8_SoundMapper implements SoundMapper {

    private static final BiMap<String, String> SOUND_MAP = HashBiMap.create();

    static {
        registerMinecraftSound("AMBIENCE_CAVE", "ambient.cave.cave");
        registerMinecraftSound("AMBIENCE_RAIN", "ambient.weather.rain");
        registerMinecraftSound("AMBIENCE_THUNDER", "ambient.weather.thunder");
        registerMinecraftSound("HURT_FLESH", "game.neutral.hurt");
        registerMinecraftSound("FALL_BIG", "game.neutral.hurt.fall.big");
        registerMinecraftSound("FALL_SMALL", "game.neutral.hurt.fall.small");
        registerMinecraftSound("DIG_WOOL", "dig.cloth");
        registerMinecraftSound("DIG_GRASS", "dig.grass");
        registerMinecraftSound("DIG_GRAVEL", "dig.gravel");
        registerMinecraftSound("DIG_SAND", "dig.sand");
        registerMinecraftSound("DIG_SNOW", "dig.snow");
        registerMinecraftSound("DIG_STONE", "dig.stone");
        registerMinecraftSound("DIG_WOOD", "dig.wood");
        registerMinecraftSound("FIRE", "fire.fire");
        registerMinecraftSound("FIRE_IGNITE", "fire.ignite");
        registerMinecraftSound("FIREWORK_BLAST", "fireworks.blast");
        registerMinecraftSound("FIREWORK_BLAST2", "fireworks.blast_far");
        registerMinecraftSound("FIREWORK_LARGE_BLAST", "fireworks.largeBlast");
        registerMinecraftSound("FIREWORK_LARGE_BLAST2", "fireworks.largeBlast_far");
        registerMinecraftSound("FIREWORK_TWINKLE", "fireworks.twinkle");
        registerMinecraftSound("FIREWORK_TWINKLE2", "fireworks.twinkle_far");
        registerMinecraftSound("FIREWORK_LAUNCH", "fireworks.launch");
        registerMinecraftSound("SPLASH2", "game.neutral.swim.splash");
        registerMinecraftSound("SWIM", "game.neutral.swim");
        registerMinecraftSound("WATER", "liquid.water");
        registerMinecraftSound("LAVA", "liquid.lava");
        registerMinecraftSound("LAVA_POP", "liquid.lavapop");
        registerMinecraftSound("MINECART_BASE", "minecart.base");
        registerMinecraftSound("MINECART_INSIDE", "minecart.inside");
        registerMinecraftSound("BAT_DEATH", "mob.bat.death");
        registerMinecraftSound("BAT_HURT", "mob.bat.hurt");
        registerMinecraftSound("BAT_IDLE", "mob.bat.idle");
        registerMinecraftSound("BAT_LOOP", "mob.bat.loop");
        registerMinecraftSound("BAT_TAKEOFF", "mob.bat.takeoff");
        registerMinecraftSound("BLAZE_BREATH", "mob.blaze.breathe");
        registerMinecraftSound("BLAZE_DEATH", "mob.blaze.death");
        registerMinecraftSound("BLAZE_HIT", "mob.blaze.hit");
        registerMinecraftSound("CAT_HISS", "mob.cat.hiss");
        registerMinecraftSound("CAT_HIT", "mob.cat.hitt");
        registerMinecraftSound("CAT_MEOW", "mob.cat.meow");
        registerMinecraftSound("CAT_PURR", "mob.cat.purr");
        registerMinecraftSound("CAT_PURREOW", "mob.cat.purreow");
        registerMinecraftSound("CHICKEN_IDLE", "mob.chicken.say");
        registerMinecraftSound("CHICKEN_HURT", "mob.chicken.hurt");
        registerMinecraftSound("CHICKEN_EGG_POP", "mob.chicken.plop");
        registerMinecraftSound("CHICKEN_WALK", "mob.chicken.step");
        registerMinecraftSound("COW_HURT", "mob.cow.hurt");
        registerMinecraftSound("COW_IDLE", "mob.cow.say");
        registerMinecraftSound("COW_WALK", "mob.cow.step");
        registerMinecraftSound("CREEPER_DEATH", "mob.creeper.death");
        registerMinecraftSound("CREEPER_HISS", "mob.creeper.say");
        registerMinecraftSound("ENDERDRAGON_DEATH", "mob.enderdragon.end");
        registerMinecraftSound("ENDERDRAGON_GROWL", "mob.enderdragon.growl");
        registerMinecraftSound("ENDERDRAGON_HIT", "mob.enderdragon.hit");
        registerMinecraftSound("ENDERDRAGON_WINGS", "mob.enderdragon.wings");
        registerMinecraftSound("ENDERMAN_DEATH", "mob.endermen.death");
        registerMinecraftSound("ENDERMAN_HIT", "mob.endermen.hit");
        registerMinecraftSound("ENDERMAN_IDLE", "mob.endermen.idle");
        registerMinecraftSound("ENDERMAN_TELEPORT", "mob.endermen.portal");
        registerMinecraftSound("ENDERMAN_SCREAM", "mob.endermen.scream");
        registerMinecraftSound("ENDERMAN_STARE", "mob.endermen.stare");
        registerMinecraftSound("GHAST_SCREAM2", "mob.ghast.affectionate_scream");
        registerMinecraftSound("GHAST_CHARGE", "mob.ghast.charge");
        registerMinecraftSound("GHAST_DEATH", "mob.ghast.death");
        registerMinecraftSound("GHAST_FIREBALL", "mob.ghast.fireball");
        registerMinecraftSound("GHAST_MOAN", "mob.ghast.moan");
        registerMinecraftSound("GHAST_SCREAM", "mob.ghast.scream");
        registerMinecraftSound("HORSE_ANGRY", "mob.horse.angry");
        registerMinecraftSound("HORSE_ARMOR", "mob.horse.armor");
        registerMinecraftSound("HORSE_BREATHE", "mob.horse.breathe");
        registerMinecraftSound("HORSE_DEATH", "mob.horse.death");
        registerMinecraftSound("HORSE_GALLOP", "mob.horse.gallop");
        registerMinecraftSound("HORSE_HIT", "mob.horse.hit");
        registerMinecraftSound("HORSE_IDLE", "mob.horse.idle");
        registerMinecraftSound("HORSE_JUMP", "mob.horse.jump");
        registerMinecraftSound("HORSE_LAND", "mob.horse.land");
        registerMinecraftSound("HORSE_SADDLE", "mob.horse.leather");
        registerMinecraftSound("HORSE_SOFT", "mob.horse.soft");
        registerMinecraftSound("HORSE_WOOD", "mob.horse.wood");
        registerMinecraftSound("DONKEY_ANGRY", "mob.horse.donkey.angry");
        registerMinecraftSound("DONKEY_DEATH", "mob.horse.donkey.death");
        registerMinecraftSound("DONKEY_HIT", "mob.horse.donkey.hit");
        registerMinecraftSound("DONKEY_IDLE", "mob.horse.donkey.idle");
        registerMinecraftSound("HORSE_SKELETON_DEATH", "mob.horse.skeleton.death");
        registerMinecraftSound("HORSE_SKELETON_HIT", "mob.horse.skeleton.hit");
        registerMinecraftSound("HORSE_SKELETON_IDLE", "mob.horse.skeleton.idle");
        registerMinecraftSound("HORSE_ZOMBIE_DEATH", "mob.horse.zombie.death");
        registerMinecraftSound("HORSE_ZOMBIE_HIT", "mob.horse.zombie.hit");
        registerMinecraftSound("HORSE_ZOMBIE_IDLE", "mob.horse.zombie.idle");
        registerMinecraftSound("IRONGOLEM_DEATH", "mob.irongolem.death");
        registerMinecraftSound("IRONGOLEM_HIT", "mob.irongolem.hit");
        registerMinecraftSound("IRONGOLEM_THROW", "mob.irongolem.throw");
        registerMinecraftSound("IRONGOLEM_WALK", "mob.irongolem.walk");
        registerMinecraftSound("MAGMACUBE_WALK", "mob.magmacube.small");
        registerMinecraftSound("MAGMACUBE_WALK2", "mob.magmacube.big");
        registerMinecraftSound("MAGMACUBE_JUMP", "mob.magmacube.jump");
        registerMinecraftSound("PIG_IDLE", "mob.pig.say");
        registerMinecraftSound("PIG_DEATH", "mob.pig.death");
        registerMinecraftSound("PIG_WALK", "mob.pig.step");
        registerMinecraftSound("SHEEP_IDLE", "mob.sheep.say");
        registerMinecraftSound("SHEEP_SHEAR", "mob.sheep.shear");
        registerMinecraftSound("SHEEP_WALK", "mob.sheep.step");
        registerMinecraftSound("SILVERFISH_HIT", "mob.silverfish.hit");
        registerMinecraftSound("SILVERFISH_KILL", "mob.silverfish.kill");
        registerMinecraftSound("SILVERFISH_IDLE", "mob.silverfish.say");
        registerMinecraftSound("SILVERFISH_WALK", "mob.silverfish.step");
        registerMinecraftSound("SKELETON_IDLE", "mob.skeleton.say");
        registerMinecraftSound("SKELETON_DEATH", "mob.skeleton.death");
        registerMinecraftSound("SKELETON_HURT", "mob.skeleton.hurt");
        registerMinecraftSound("SKELETON_WALK", "mob.skeleton.step");
        registerMinecraftSound("SLIME_ATTACK", "mob.slime.attack");
        registerMinecraftSound("SLIME_WALK", "mob.slime.small");
        registerMinecraftSound("SLIME_WALK2", "mob.slime.big");
        registerMinecraftSound("SPIDER_IDLE", "mob.spider.say");
        registerMinecraftSound("SPIDER_DEATH", "mob.spider.death");
        registerMinecraftSound("SPIDER_WALK", "mob.spider.step");
        registerMinecraftSound("VILLAGER_DEATH", "mob.villager.death");
        registerMinecraftSound("VILLAGER_HAGGLE", "mob.villager.haggle");
        registerMinecraftSound("VILLAGER_HIT", "mob.villager.hit");
        registerMinecraftSound("VILLAGER_IDLE", "mob.villager.idle");
        registerMinecraftSound("VILLAGER_NO", "mob.villager.no");
        registerMinecraftSound("VILLAGER_YES", "mob.villager.yes");
        registerMinecraftSound("WITHER_DEATH", "mob.wither.death");
        registerMinecraftSound("WITHER_HURT", "mob.wither.hurt");
        registerMinecraftSound("WITHER_IDLE", "mob.wither.idle");
        registerMinecraftSound("WITHER_SHOOT", "mob.wither.shoot");
        registerMinecraftSound("WITHER_SPAWN", "mob.wither.spawn");
        registerMinecraftSound("WOLF_BARK", "mob.wolf.bark");
        registerMinecraftSound("WOLF_DEATH", "mob.wolf.death");
        registerMinecraftSound("WOLF_GROWL", "mob.wolf.growl");
        registerMinecraftSound("WOLF_HOWL", "mob.wolf.howl");
        registerMinecraftSound("WOLF_HURT", "mob.wolf.hurt");
        registerMinecraftSound("WOLF_PANT", "mob.wolf.panting");
        registerMinecraftSound("WOLF_SHAKE", "mob.wolf.shake");
        registerMinecraftSound("WOLF_WALK", "mob.wolf.step");
        registerMinecraftSound("WOLF_WHINE", "mob.wolf.whine");
        registerMinecraftSound("ZOMBIE_METAL", "mob.zombie.metal");
        registerMinecraftSound("ZOMBIE_WOOD", "mob.zombie.wood");
        registerMinecraftSound("ZOMBIE_WOODBREAK", "mob.zombie.woodbreak");
        registerMinecraftSound("ZOMBIE_IDLE", "mob.zombie.say");
        registerMinecraftSound("ZOMBIE_DEATH", "mob.zombie.death");
        registerMinecraftSound("ZOMBIE_HURT", "mob.zombie.hurt");
        registerMinecraftSound("ZOMBIE_INFECT", "mob.zombie.infect");
        registerMinecraftSound("ZOMBIE_UNFECT", "mob.zombie.unfect");
        registerMinecraftSound("ZOMBIE_REMEDY", "mob.zombie.remedy");
        registerMinecraftSound("ZOMBIE_WALK", "mob.zombie.step");
        registerMinecraftSound("ZOMBIE_PIG_IDLE", "mob.zombiepig.zpig");
        registerMinecraftSound("ZOMBIE_PIG_ANGRY", "mob.zombiepig.zpigangry");
        registerMinecraftSound("ZOMBIE_PIG_DEATH", "mob.zombiepig.zpigdeath");
        registerMinecraftSound("ZOMBIE_PIG_HURT", "mob.zombiepig.zpighurt");
        registerMinecraftSound("NOTE_BASS_GUITAR", "note.bassattack");
        registerMinecraftSound("NOTE_SNARE_DRUM", "note.snare");
        registerMinecraftSound("NOTE_PLING", "note.pling");
        registerMinecraftSound("NOTE_BASS", "note.bass");
        registerMinecraftSound("NOTE_PIANO", "note.harp");
        registerMinecraftSound("NOTE_BASS_DRUM", "note.bd");
        registerMinecraftSound("NOTE_STICKS", "note.hat");
        registerMinecraftSound("PORTAL", "portal.portal");
        registerMinecraftSound("PORTAL_TRAVEL", "portal.travel");
        registerMinecraftSound("PORTAL_TRIGGER", "portal.trigger");
        registerMinecraftSound("ANVIL_BREAK", "random.anvil_break");
        registerMinecraftSound("ANVIL_LAND", "random.anvil_land");
        registerMinecraftSound("ANVIL_USE", "random.anvil_use");
        registerMinecraftSound("SHOOT_ARROW", "random.bow");
        registerMinecraftSound("ARROW_HIT", "random.bowhit");
        registerMinecraftSound("ITEM_BREAK", "random.break");
        registerMinecraftSound("BURP", "random.burp");
        registerMinecraftSound("CHEST_CLOSE", "random.chestclosed");
        registerMinecraftSound("CHEST_OPEN", "random.chestopen");
        registerMinecraftSound("CLICK", "random.click");
        registerMinecraftSound("DOOR_CLOSE", "random.door_close");
        registerMinecraftSound("DOOR_OPEN", "random.door_open");
        registerMinecraftSound("DRINK", "random.drink");
        registerMinecraftSound("EAT", "random.eat");
        registerMinecraftSound("EXPLODE", "random.explode");
        registerMinecraftSound("FIZZ", "random.fizz");
        registerMinecraftSound("FUSE", "creeper.primed");
        registerMinecraftSound("GLASS", "dig.glass");
        registerMinecraftSound("LEVEL_UP", "random.levelup");
        registerMinecraftSound("ORB_PICKUP", "random.orb");
        registerMinecraftSound("ITEM_PICKUP", "random.pop");
        registerMinecraftSound("SPLASH", "random.splash");
        registerMinecraftSound("SUCCESSFUL_HIT", "random.successful_hit");
        registerMinecraftSound("WOOD_CLICK", "random.wood_click");
        registerMinecraftSound("STEP_WOOL", "step.cloth");
        registerMinecraftSound("STEP_GRASS", "step.grass");
        registerMinecraftSound("STEP_GRAVEL", "step.gravel");
        registerMinecraftSound("STEP_LADDER", "step.ladder");
        registerMinecraftSound("STEP_SAND", "step.sand");
        registerMinecraftSound("STEP_SNOW", "step.snow");
        registerMinecraftSound("STEP_STONE", "step.stone");
        registerMinecraftSound("STEP_WOOD", "step.wood");
        registerMinecraftSound("PISTON_EXTEND", "tile.piston.out");
        registerMinecraftSound("PISTON_RETRACT", "tile.piston.in");
    }

    private static void registerMinecraftSound(String bukkitSound, String minecraftSound) {
        SOUND_MAP.put("minecraft:" + minecraftSound, bukkitSound);
    }

    /**
     * Gets the Bukkit Sound enum name for the given Minecraft sound identifier.
     *
     * @param soundName The Minecraft sound identifier (e.g., "mob.zombie.say")
     * @return The Bukkit Sound enum name as a String, or null if not found
     */
    @Override
    public String mapSoundName(String soundName) {
        return SOUND_MAP.get(soundName);
    }

    /**
     * Gets the Minecraft sound identifier for the given Bukkit Sound enum name.
     *
     * @param bukkitSoundName The Bukkit Sound enum name
     * @return The Minecraft sound identifier as a String, or null if not found
     */
    @Override
    public String reverseMapSoundName(String bukkitSoundName) {
        return SOUND_MAP.inverse().get(bukkitSoundName);
    }

}