package me.bobulo.mine.pdam.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class EventUtils {

    private EventUtils() {
    }

    public static void callEvent(Event event) {
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void registerEventListener(Object listener) {
        MinecraftForge.EVENT_BUS.register(listener);
    }

}
