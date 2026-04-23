package me.bobulo.mine.pdam.feature.update;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ChatUpdateWarning {

    public static final String GITHUB_URL_DOWNLOAD = "https://github.com/%s/%s/releases/latest";

    private final UpdateChecker updateChecker;
    private boolean alreadyWarned = false;

    public ChatUpdateWarning(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (alreadyWarned || event.entity != Minecraft.getMinecraft().thePlayer) {
            return;
        }

        if (!updateChecker.isUpdateAvailable() || !Update.CHAT_WARNING.get()) {
            return;
        }

        String latestVersion = updateChecker.getLatestVersion();
        String url = String.format(GITHUB_URL_DOWNLOAD, Update.GITHUB_USER, Update.GITHUB_REPO);

        ChatStyle style = new ChatStyle();
        style.setColor(EnumChatFormatting.AQUA);
        style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

        ChatComponentText text = new ChatComponentText("A new version of PDAM is available: " +
          latestVersion + ". Click here to download!");
        text.setChatStyle(style);

        Minecraft.getMinecraft().thePlayer.addChatMessage(text);
        alreadyWarned = true;
    }
}