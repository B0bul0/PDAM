package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.PDAM;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigListener {

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!PDAM.MOD_ID.equals(event.modID)) {
            return;
        }

        PDAM.getConfigService().syncAll();
    }

}
