package me.bobulo.mine.pdam.feature.packet.data.server;

import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import org.jetbrains.annotations.NotNull;

public final class SoundEffectServerPacketData implements ServerPacketData {

    private static final String PACKET_NAME = "SoundEffect";

    private String soundName;
    private int posX;
    private int posY;
    private int posZ;
    private float soundVolume;
    private int soundPitch;

    @Override
    public @NotNull String getPacketName() {
        return PACKET_NAME;
    }

    public static class Extractor implements PacketDataExtractor<SoundEffectServerPacketData, S29PacketSoundEffect> {

        @Override
        public @NotNull SoundEffectServerPacketData extract(@NotNull S29PacketSoundEffect packet) {
            SoundEffectServerPacketData data = new SoundEffectServerPacketData();
            data.soundName = packet.getSoundName();
            data.posX = (int) packet.getX();
            data.posY = (int) packet.getY();
            data.posZ = (int) packet.getZ();
            data.soundVolume = packet.getVolume();
            data.soundPitch = (int) (packet.getPitch() * 63.0F);
            return data;
        }

    }

}

