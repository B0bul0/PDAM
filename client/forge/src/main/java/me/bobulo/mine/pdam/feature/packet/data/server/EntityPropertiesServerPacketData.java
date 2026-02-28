package me.bobulo.mine.pdam.feature.packet.data.server;

import com.google.common.collect.Lists;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class EntityPropertiesServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "EntityProperties";

    private int entityId;
    private final List<Snapshot> snapshots = Lists.newArrayList();

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityPropertiesServerPacketData, S20PacketEntityProperties> {

        @Override
        public @NotNull EntityPropertiesServerPacketData extract(@NotNull S20PacketEntityProperties packet) {
            EntityPropertiesServerPacketData data = new EntityPropertiesServerPacketData();
            data.entityId = packet.getEntityId();

            for (S20PacketEntityProperties.Snapshot snapshot : packet.func_149441_d()) {
                data.snapshots.add(new Snapshot(snapshot));
            }
            return data;
        }

    }

    public static class Snapshot {

        private final String name;
        private final double baseValue;
        private final Collection<Modifier> modifiers;

        public Snapshot(S20PacketEntityProperties.Snapshot snapshot) {
            this.name = snapshot.func_151409_a();
            this.baseValue = snapshot.func_151410_b();
            this.modifiers = Lists.newArrayList();
            for (AttributeModifier modifier : snapshot.func_151408_c()) {
                this.modifiers.add(new Modifier(modifier));
            }
        }
    }

    public static class Modifier {

        private final UUID uuid;
        private final double amount;
        private final int operation;

        public Modifier(AttributeModifier modifier) {
            this.uuid = modifier.getID();
            this.amount = modifier.getAmount();
            this.operation = modifier.getOperation();
        }
    }

}

