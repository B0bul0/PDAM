package me.bobulo.mine.pdam.feature.packet.data.entity.populator;

import me.bobulo.mine.pdam.feature.packet.data.entity.EntityMetadata;
import me.bobulo.mine.pdam.feature.packet.data.entity.acessor.DataAccessor;
import org.jetbrains.annotations.NotNull;

/**
 * A generic interface for populating entity metadata.
 */
public interface MetadataPopulator<T extends EntityMetadata> {

    /**
     * Populates the given metadata instance using data from the provided accessor.
     *
     * @param metadata The metadata instance to populate
     * @param accessor The data accessor to read data from
     */
    void populate(@NotNull T metadata, @NotNull DataAccessor accessor);

}
