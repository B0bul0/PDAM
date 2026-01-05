package me.bobulo.mine.pdam.feature.packet.data.client;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.ReflectionUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

public final class UseEntityClientPacketData implements ClientPacketData {

    private static final String PACKET_NAME = "UseEntity";

    private int entityId;
    private C02PacketUseEntity.Action action;
    private Vec3 hitVec;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<UseEntityClientPacketData, C02PacketUseEntity> {

        @Override
        public @NotNull UseEntityClientPacketData extract(@NotNull C02PacketUseEntity packet) {
            UseEntityClientPacketData data = new UseEntityClientPacketData();
            data.entityId = ReflectionUtils.getFieldValue(packet, "entityId");
            data.action = packet.getAction();
            data.hitVec = packet.getHitVec();
            return data;
        }

    }

}

