package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import org.jetbrains.annotations.NotNull;

public final class SpawnGlobalEntityServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SpawnGlobalEntity";

    private int entityId;
    private int x;
    private int y;
    private int z;
    private int type;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SpawnGlobalEntityServerPacketData, S2CPacketSpawnGlobalEntity> {

        @Override
        public @NotNull SpawnGlobalEntityServerPacketData extract(@NotNull S2CPacketSpawnGlobalEntity packet) {
            SpawnGlobalEntityServerPacketData data = new SpawnGlobalEntityServerPacketData();
            data.entityId = packet.func_149052_c();
            data.x = packet.func_149051_d();
            data.y = packet.func_149050_e();
            data.z = packet.func_149049_f();
            data.type = packet.func_149053_g();
            return data;
        }

    }

}

