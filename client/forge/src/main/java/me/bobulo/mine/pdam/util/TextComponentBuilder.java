package me.bobulo.mine.pdam.util;

import me.bobulo.mine.pdam.command.CopyToClipboardCommand;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;

public final class TextComponentBuilder {

    private final IChatComponent mainComponent;
    private IChatComponent component;

    private TextComponentBuilder(IChatComponent component) {
        this.mainComponent = component;
        this.component = component;
    }

    public static TextComponentBuilder create(String text) {
        return new TextComponentBuilder(new ChatComponentText(text));
    }

    public static TextComponentBuilder create(IChatComponent chatComponent) {
        return new TextComponentBuilder(chatComponent);
    }

    public static TextComponentBuilder create(TextComponentBuilder builder) {
        return new TextComponentBuilder(builder.build());
    }

    public static TextComponentBuilder createEmpty() {
        return new TextComponentBuilder(new ChatComponentText(""));
    }

    public static TextComponentBuilder createTranslated(String translationKey, Object... args) {
        return new TextComponentBuilder(new ChatComponentTranslation(translationKey, args));
    }

    public TextComponentBuilder append(String text) {
        append(new ChatComponentText(text));
        return this;
    }

    public TextComponentBuilder appendTranslated(String translationKey, Object... args) {
        append(new ChatComponentTranslation(translationKey, args));
        return this;
    }

    public TextComponentBuilder append(TextComponentBuilder builder) {
        return append(builder.build());
    }

    public TextComponentBuilder appendNewLine() {
        append(new ChatComponentText("\n"));
        return this;
    }

    public TextComponentBuilder append(IChatComponent chatComponent) {
        component.appendSibling(chatComponent);
        component = chatComponent;
        return this;
    }

    public TextComponentBuilder withChatStyle(ChatStyle chatStyle) {
        component.setChatStyle(chatStyle);
        return this;
    }

    public TextComponentBuilder withColor(EnumChatFormatting color) {
        component.getChatStyle().setColor(color);
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

    public TextComponentBuilder withHover(TextComponentBuilder builder) {
        return withHover(builder.build());
    }

    public TextComponentBuilder withHover(IChatComponent chatComponent) {
        component.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, chatComponent));
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
        return mainComponent;
    }

}