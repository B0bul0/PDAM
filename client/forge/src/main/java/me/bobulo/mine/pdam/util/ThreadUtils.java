package me.bobulo.mine.pdam.util;

import net.minecraft.client.Minecraft;

public final class ThreadUtils {

    private ThreadUtils() {
    }

    public static boolean isMainThread() {
        return Minecraft.getMinecraft().isCallingFromMinecraftThread();
    }

    public static void ensureMainThread(Runnable task) {
        if (isMainThread()) {
            task.run();
        } else {
            Minecraft.getMinecraft().addScheduledTask(task);
        }
    }

    public static void runOnMainThread(Runnable task) {
        Minecraft.getMinecraft().addScheduledTask(task);
    }

}
