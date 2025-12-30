package me.bobulo.mine.pdam.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public final class TextComponentBuilder {

    private final IChatComponent component;

    private TextComponentBuilder(IChatComponent component) {
        this.component = component;
    }

    public static TextComponentBuilder create(String text) {
        return new TextComponentBuilder(new ChatComponentText(text));
    }

    public static TextComponentBuilder createTranslated(String translationKey, Object... args) {
        return new TextComponentBuilder(new ChatComponentTranslation(translationKey, args));
    }

    public TextComponentBuilder append(String text) {
        component.appendSibling(new ChatComponentText(text));
        return this;
    }

    public TextComponentBuilder appendTranslated(String translationKey, Object... args) {
        component.appendSibling(new ChatComponentTranslation(translationKey, args));
        return this;
    }

    public TextComponentBuilder append(TextComponentBuilder builder) {
        component.appendSibling(builder.build());
        return this;
    }

    public TextComponentBuilder withHover(String text) {
        IChatComponent hoverText = new ChatComponentText(text);
        component.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        return this;
    }

    public TextComponentBuilder withHoverTranslated(String translationKey, Object... args) {
        IChatComponent hoverText = new ChatComponentTranslation(translationKey, args);
        component.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        return this;
    }

    public TextComponentBuilder withClick(ClickEvent.Action action, String value) {
        component.getChatStyle().setChatClickEvent(new ClickEvent(action, value));
        return this;
    }

    public TextComponentBuilder withClickRunCommand(String command) {
        return withClick(ClickEvent.Action.RUN_COMMAND, command);
    }

    public TextComponentBuilder withClickSuggestCommand(String command) {
        return withClick(ClickEvent.Action.SUGGEST_COMMAND, command);
    }

    public TextComponentBuilder withClickOpenURL(String url) {
        return withClick(ClickEvent.Action.OPEN_URL, url);
    }

    public TextComponentBuilder withClickCopyToClipboard(String text) {
        return withClick(ClickEvent.Action.RUN_COMMAND, "/" + CopyToClipboardCommand.COMMAND_NAME + " " + text);
    }

    public void sendToPlayer(EntityPlayerSP player) {
        if (player != null) {
            player.addChatMessage(build());
        }
    }

    public void sendToClient() {
        EntityPlayerSP player = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
        sendToPlayer(player);
    }

    public IChatComponent build() {
        return component;
    }

}