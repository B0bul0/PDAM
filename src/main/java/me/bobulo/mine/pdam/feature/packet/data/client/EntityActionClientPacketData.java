package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.jetbrains.annotations.NotNull;

public final class EntityActionClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "EntityAction";

    private int entityID;
    private C0BPacketEntityAction.Action action;
    private int auxData;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<EntityActionClientPacketData, C0BPacketEntityAction> {

        @Override
        public @NotNull EntityActionClientPacketData extract(@NotNull C0BPacketEntityAction packet) {
            EntityActionClientPacketData data = new EntityActionClientPacketData();
            data.entityID = ReflectionUtils.getFieldValue(packet, "entityID");
            data.action = packet.getAction();
            data.auxData = packet.getAuxData();
            return data;
        }

    }

}

