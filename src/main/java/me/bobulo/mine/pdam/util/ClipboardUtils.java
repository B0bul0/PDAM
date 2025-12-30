package me.bobulo.mine.pdam.util;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClipboardUtils {

    private ClipboardUtils() {
    }

    public static void copyToClipboard(String text) {
        GuiScreen.setClipboardString(text);
    }

}
