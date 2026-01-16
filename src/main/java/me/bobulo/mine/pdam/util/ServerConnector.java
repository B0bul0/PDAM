package me.bobulo.mine.pdam.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.math.NumberUtils;

public final class ServerConnector {

    public static final int DEFAULT_MINECRAFT_PORT = 25565;

    public static void connectToServer(String serverIP) {
        if (serverIP.contains(":")) {
            String[] parts = serverIP.split(":");
            String ip = parts[0];
            int port = NumberUtils.toInt(parts[1], DEFAULT_MINECRAFT_PORT);
            connectToServer(ip, port);
        } else {
            connectToServer(serverIP, DEFAULT_MINECRAFT_PORT);
        }
    }

    public static void connectToServer(String ip, int port) {
        ServerData data = new ServerData("Server", ip + ":" + port, false);
        connectToServer(data);
    }

    public static void connectToServer(ServerData serverData) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld != null) {
            mc.theWorld.sendQuittingDisconnectingPacket();

            mc.loadWorld(null);

            if (mc.getNetHandler() != null) {
                mc.getNetHandler().getNetworkManager().closeChannel(new ChatComponentText("Connecting to new server"));
            }
        }

        mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, serverData));
    }

    private ServerConnector() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}