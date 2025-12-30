package me.bobulo.mine.pdam.feature.chat;

import me.bobulo.mine.pdam.ui.notification.NotificationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;
import java.util.List;

import static me.bobulo.mine.pdam.util.LocaleUtils.translateToLocal;

public class ChatCopyListener {

    private static final Logger log = LogManager.getLogger(ChatCopyListener.class);

    private Field drawnChatLinesField;

    public ChatCopyListener() {
        try {
            drawnChatLinesField = GuiNewChat.class.getDeclaredField("drawnChatLines");
            drawnChatLinesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            log.warn("Could not access drawnChatLines field in GuiNewChat", e);
        }
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!(event.gui instanceof GuiChat) || drawnChatLinesField == null) {
            return;
        }

        boolean isLeftClick = Mouse.getEventButton() == 0 && Mouse.getEventButtonState();
        boolean isCtrlDown = GuiScreen.isCtrlKeyDown();

        if (!isLeftClick || !isCtrlDown) {
            return;
        }

        GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        IChatComponent component = chatGUI.getChatComponent(Mouse.getX(), Mouse.getY());

        if (component == null) {
            return;
        }

        try {
            List<ChatLine> drawnChatLines = (List<ChatLine>) drawnChatLinesField.get(chatGUI);

            for (ChatLine chatLine : drawnChatLines) {
                if (isComponentInLine(chatLine.getChatComponent(), component)) {
                    // Copy to clipboard
                    GuiScreen.setClipboardString(chatLine.getChatComponent().getUnformattedText());
                    event.setCanceled(true);
                    log.info("Copied chat line to clipboard: {}", chatLine.getChatComponent().getUnformattedText());
                    NotificationManager.showSuccess(translateToLocal("pdam.general.copied_to_clipboard"));
                    return;
                }
            }

        } catch (IllegalAccessException e) {
            log.warn("Could not access drawnChatLines field in GuiNewChat", e);
        }
    }

    private boolean isComponentInLine(IChatComponent source, IChatComponent target) {
        if (source.equals(target)) {
            return true;
        }

        for (IChatComponent sibling : source.getSiblings()) {
            if (isComponentInLine(sibling, target)) {
                return true;
            }
        }

        return false;
    }
}
