package me.bobulo.mine.pdam.feature.packet.metadata;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface PacketMetadata extends Serializable {

    @NotNull String getPacketName();

}