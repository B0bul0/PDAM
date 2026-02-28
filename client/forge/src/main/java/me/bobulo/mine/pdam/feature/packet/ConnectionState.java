package me.bobulo.mine.pdam.feature.packet;

import net.minecraft.network.EnumConnectionState;

public enum ConnectionState {

    HANDSHAKING,
    PLAY,
    LOGIN,
    STATUS;

    public static ConnectionState fromNMS(EnumConnectionState nmsState) {
        if (nmsState == null) {
            return null;
        }

        switch (nmsState) {
            case HANDSHAKING:
                return HANDSHAKING;
            case PLAY:
                return PLAY;
            case STATUS:
                return STATUS;
            case LOGIN:
                return LOGIN;
            default:
                return null;
        }
    }

}
