package me.bobulo.mine.pdam.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.jetbrains.annotations.NotNull;

public final class PlayerUtils {

    private PlayerUtils() {
    }

    public static void teleportViaServer(double x, double y, double z) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        player.sendChatMessage("/tp " + x + " " + y + " " + z);
    }

    /**
     * Send a server chat message as the player.
     * The message is sent to the server as if the player typed it in chat.
     */
    public static void sendChatMessage(@NotNull String message) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        player.sendChatMessage(message);
    }

    /**
     * Gives the player the specified item in their current hotbar slot.
     * Only works in creative mode.
     *
     * @param item The item to give the player.
     */
    public static void giveItem(@NotNull ItemStack item) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.playerController == null || !mc.playerController.isInCreativeMode()) { // only works in creative
            return;
        }

        if (mc.thePlayer == null) {
            return;
        }

        int currentItem = mc.thePlayer.inventory.currentItem + 36; // hotbar slots are 36-44

        NetHandlerPlayClient netHandler = mc.getNetHandler();
        netHandler.addToSendQueue(new C10PacketCreativeInventoryAction(currentItem, item));
    }

    /**
     * Gives the player the specified item in the given slot.
     * Only works in creative mode.
     *
     * @param slot hotbar slots: 0-8, inventory slots: 9-44
     * @param item The item to give the player.
     */
    public static void giveItem(int slot, @NotNull ItemStack item) {
        int rawSlot = slot;
        if (slot >= 0 && slot <= 8) { // hotbar slots
            rawSlot += 36;
        }

        if (slot < 0 || rawSlot > 44) {
            throw new IllegalArgumentException("Slot must be between 0-8 (hotbar) or 9-44 (inventory)");
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.playerController == null || !mc.playerController.isInCreativeMode()) { // only works in creative
            return;
        }

        NetHandlerPlayClient netHandler = mc.getNetHandler();
        netHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slot, item));
    }

}