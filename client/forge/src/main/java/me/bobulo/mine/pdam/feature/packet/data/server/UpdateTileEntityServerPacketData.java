package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.nbt.NBTData;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public final class UpdateTileEntityServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "UpdateTileEntity";

    private BlockPosition pos;
    private int metadata;
    private Map<String, Object> nbt;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<UpdateTileEntityServerPacketData, S35PacketUpdateTileEntity> {

        @Override
        public @NotNull UpdateTileEntityServerPacketData extract(@NotNull S35PacketUpdateTileEntity packet) throws IOException {
            UpdateTileEntityServerPacketData data = new UpdateTileEntityServerPacketData();
            data.pos = BlockPosition.from(packet.getPos());
            data.metadata = packet.getTileEntityType();
            data.nbt = NBTData.from(packet.getNbtCompound());
            return data;
        }

    }

}

