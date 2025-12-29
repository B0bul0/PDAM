package me.bobulo.mine.pdam.feature.sound;

import me.bobulo.mine.pdam.config.ConfigInitContext;
import me.bobulo.mine.pdam.feature.component.AbstractFeatureComponent;
import me.bobulo.mine.pdam.util.LocaleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;

public final class SoundDebugFeatureComponent extends AbstractFeatureComponent {

    private SoundDebugListener listener;

    private String[] whitelist;
    private String[] blacklist;

    @Override
    protected void onEnable() {
        if (this.listener == null) {
            this.listener = new SoundDebugListener(this);
        }

        MinecraftForge.EVENT_BUS.register(this.listener);
    }

    @Override
    protected void onDisable() {
        if (this.listener == null) {
            return;
        }

        MinecraftForge.EVENT_BUS.unregister(this.listener);
        this.listener = null;
    }

    @Override
    public void initProperties(ConfigInitContext context) {
        context.createProperty("whitelist", new String[0])
          .comment("List of sound names to always allow (empty = allow all)")
          .onUpdate((newVal) -> this.whitelist = newVal);

        context.createProperty("blacklist", new String[0])
          .comment("List of sound names to block")
          .onUpdate((newVal) -> this.blacklist = newVal);
    }

    private boolean matches(String[] list, String soundName) {
        if (list == null) {
            return false;
        }

        for (String pattern : list) {
            if (pattern.contains("*")) {

                String regex = pattern.replace(".", "\\.").replace("*", ".*");
                if (soundName.matches(regex)) {
                    return true;
                }
            } else {

                if (pattern.equals(soundName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean filter(String soundName) {
        if (soundName == null) {
            return false;
        }

        if (matches(blacklist, soundName)) {
            return false;
        }

        if (whitelist != null && whitelist.length > 0) {
            return matches(whitelist, soundName);
        }

        return true;
    }

    public void sendDebugMessage(String sound, float pitch, float volume) {
        if (!filter(sound)) {
            return;
        }

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        LocaleUtils.sendMessage(
          "pdam.sound_debug.debug_message",
          sound,
          String.format("%.2f", pitch),
          String.format("%.2f", volume)
        );
    }

}
