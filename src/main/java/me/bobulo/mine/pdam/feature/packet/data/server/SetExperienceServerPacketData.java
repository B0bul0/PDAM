package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import org.jetbrains.annotations.NotNull;

public final class SetExperienceServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SetExperience";

    private float experienceBar;
    private int level;
    private int totalExperience;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SetExperienceServerPacketData, S1FPacketSetExperience> {

        @Override
        public @NotNull SetExperienceServerPacketData extract(@NotNull S1FPacketSetExperience packet) {
            SetExperienceServerPacketData data = new SetExperienceServerPacketData();
            data.experienceBar = packet.func_149397_c();
            data.level = packet.getLevel();
            data.totalExperience = packet.getTotalExperience();
            return data;
        }

    }

}

