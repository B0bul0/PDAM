package me.bobulo.mine.pdam.feature.packet.metadata.entity.factory;

import me.bobulo.mine.pdam.feature.packet.metadata.entity.*;
import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataAccessor;
import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.DataWatcherAccessor;
import me.bobulo.mine.pdam.feature.packet.metadata.entity.acessor.WatchableListAccessor;
import net.minecraft.entity.DataWatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class EntityMetadataFactory {

    private static final Logger log = LogManager.getLogger(EntityMetadataFactory.class);

    /**
     * Creates metadata from a list of WatchableObjects.
     */
    public static <T extends EntityMetadata> T create(@NotNull List<DataWatcher.WatchableObject> watchableObjects, @NotNull Class<T> clazz) {
        return create(new WatchableListAccessor(watchableObjects), clazz);
    }

    /**
     * Creates metadata from a DataWatcher.
     */
    public static <T extends EntityMetadata> T create(@NotNull DataWatcher dataWatcher, @NotNull Class<T> clazz) {
        return create(new DataWatcherAccessor(dataWatcher), clazz);
    }

    /**
     * Creates base EntityMetadata from DataWatcher.
     */
    public static EntityMetadata create(DataWatcher dataWatcher) {
        return create(dataWatcher, EntityMetadata.class);
    }

    /**
     * Creates base EntityMetadata from WatchableObject list.
     */
    public static EntityMetadata create(List<DataWatcher.WatchableObject> watchableObjects) {
        return create(watchableObjects, EntityMetadata.class);
    }

    private static <T extends EntityMetadata> T create(DataAccessor accessor, Class<T> clazz) {
        try {
            T instance = clazz.getConstructor().newInstance();
            instance.populate(accessor);
            return instance;
        } catch (Exception e) {
            log.error("Failed to create EntityMetadata instance of class: {}", clazz.getName(), e);
            return null;
        }
    }

}