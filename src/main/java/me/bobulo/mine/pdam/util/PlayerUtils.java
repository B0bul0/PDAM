package me.bobulo.mine.pdam.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public final class PlayerUtils {

    private PlayerUtils() {
    }

    public static void teleportViaServer(double x, double y, double z) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        player.sendChatMessage("/tp " + x + " " + y + " " + z);
    }

}