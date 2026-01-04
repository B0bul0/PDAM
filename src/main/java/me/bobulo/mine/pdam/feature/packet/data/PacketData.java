package me.bobulo.mine.pdam.feature.packet.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface PacketData extends Serializable {

    @NotNull String getPacketName();

}