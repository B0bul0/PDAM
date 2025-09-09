package me.bobulo.mine.devmod.config;

import me.bobulo.mine.devmod.DevMod;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigListener {

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!DevMod.MOD_ID.equals(event.modID)) {
            return;
        }

        DevMod.getConfigService().syncAll();
    }

}
