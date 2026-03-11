package me.bobulo.mine.pdam.feature.hitbox;

import lombok.Getter;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.config.ConfigValue;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class HitBoxes {

    public static final String FEATURE_ID = "hitboxes";

    public static final int DEFAULT_COLOR = Color.WHITE.getRGB();

    // The color of the hitbox outline.
    public static final ConfigValue<Integer> COLOR = ConfigProperty.of(FEATURE_ID + ".color", DEFAULT_COLOR);

    // Whether to only render the hitbox of the entity the player is currently looking at, or all entities.
    public static final ConfigValue<Boolean> ONLY_TARGET = ConfigProperty.of(FEATURE_ID + ".only_target", false);

    // Show the hitbox of invisible entities.
    public static final ConfigValue<Boolean> SHOW_INVISIBLE = ConfigProperty.of(FEATURE_ID + ".show_invisible", false);

    // Show the expanded hitbox (bounding box + attack bounding box) for living entities.
    public static final ConfigValue<Boolean> SHOW_EXPANDED = ConfigProperty.of(FEATURE_ID + ".show_expanded", true);

    public static final ConfigValue<Boolean> ENABLE_ENTITY_TYPES = ConfigProperty.of(FEATURE_ID + ".entities.enabled", false);
    private static final Map<String, EntityConfig> ENTITIES = new HashMap<>();

    public static EntityConfig getByEntity(String entityType) {
        return ENTITIES.computeIfAbsent(entityType, EntityConfig::new);
    }

    public static boolean isEnabledForEntity(String entityType) {
        return ENABLE_ENTITY_TYPES.get() && getByEntity(entityType).enabled.get();
    }

    private HitBoxes() {}

    public static class EntityConfig {
        private final String entityType;
        public final ConfigValue<Boolean> enabled;
        public final ConfigValue<Integer> color;

        public EntityConfig(String entityType) {
            this.entityType = entityType;
            this.enabled = ConfigProperty.of(FEATURE_ID + ".entities." + entityType + ".enabled", true);
            this.color = ConfigProperty.of(FEATURE_ID + ".entities." + entityType + ".color", DEFAULT_COLOR);
        }
    }

}
