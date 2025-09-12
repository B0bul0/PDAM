package me.bobulo.mine.devmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

public final class LocaleUtils {

    private LocaleUtils() {
    }

    public static void sendMessage(String translationKey, Object... args) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player.addChatMessage(new ChatComponentTranslation(translationKey, args));
        }
    }

    public static String translateToLocal(String translationKey) {
        return StatCollector.translateToLocal(translationKey);
    }

    public static String translateToLocal(String translationKey, Object... args) {
        return StatCollector.translateToLocalFormatted(translationKey, args);
    }

}