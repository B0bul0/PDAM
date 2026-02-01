package me.bobulo.mine.pdam.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

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
        return I18n.format(translationKey);
    }

    public static String translateToLocal(String translationKey, Object... args) {
        return I18n.format(translationKey, args);
    }

}