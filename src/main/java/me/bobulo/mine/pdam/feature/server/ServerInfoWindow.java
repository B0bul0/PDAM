package me.bobulo.mine.pdam.feature.server;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.MCFontImGui.mcText;

public class ServerInfoWindow extends AbstractRenderItemWindow {

    private static final Logger log = LogManager.getLogger(ServerInfoWindow.class);
    private final ServerScanner serverScanner = new ServerScanner();
    private final ImGuiTextFilter serverFilter = new ImGuiTextFilter();

    private final ImBoolean rawMOTD = new ImBoolean(true);
    private final ImString motd = new ImString();

    public ServerInfoWindow() {
        super("Server Info");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(380, 550, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Server Info###ServerInfoWindow", isVisible)) {
            renderContent();
        }

        end();
    }

    private void renderContent() {
        if (beginTabBar("ServerInfoTabs")) {

            if (beginTabItem("Current Server")) {
                renderCurrentServer();
                endTabItem();
            }

            if (beginTabItem("Server List")) {
                if (beginChild("ServerListChild", 0, 0, false)) {
                    renderServerList();
                    endChild();
                }
                endTabItem();
            }

            endTabBar();
        }
    }

    private void renderCurrentServer() {
        ServerData currentServer = getCurrentServer();
        if (currentServer == null) {
            text("Not connected to any server.");
            return;
        }

        renderServer(currentServer);
    }

    private void renderServerList() {
        List<ServerData> serverList = serverScanner.getServerCache();
        if (serverList.isEmpty()) {
            text("No servers found in server list.");

            if (button("Refresh Server List")) {
                serverScanner.refreshServerList();
            }

            return;
        }

        text("Filter Servers:");
        serverFilter.draw();

        if (button("Ping Servers")) {
            serverScanner.pingServerList(serverData -> serverFilter.passFilter(serverData.serverName + " " + serverData.serverIP));
        }

        if (serverScanner.isPinging()) {
            sameLine();
            text("Pinging servers...");
        }

        separatorText("Servers");

        int index = 0;
        for (ServerData serverData : serverList) {
            if (!serverFilter.passFilter(serverData.serverName + " " + serverData.serverIP)) {
                continue;
            }

            if (collapsingHeader(serverData.serverName + "##serverHeader_" + index++)) {
                pushID("server_" + index);
                renderServer(serverData);
                popID();
            }
        }
    }

    private void renderServer(ServerData serverData) {
        if (serverData == null) {
            return;
        }

        separatorText("Server Info");

        text("Server Name:");
        sameLine();
        text(serverData.serverName);

        text("Server IP:");
        sameLine();
        text(serverData.serverIP);

        text("Ping:");
        sameLine();
        text(serverData.pingToServer + "ms");

        text("Population Info:");
        sameLine();
        mcText(serverData.populationInfo, 1.2F);

        separatorText("MOTD");
        checkbox("Raw MOTD", rawMOTD);

        if (serverData.serverMOTD == null) {
            text("No MOTD available.");
        } else if (rawMOTD.get()) {
            motd.set(serverData.serverMOTD);
            inputTextMultiline("##RawMotd", motd, 400, 50, ImGuiInputTextFlags.ReadOnly);
        } else {
            for (String line : serverData.serverMOTD.split("\n")) {
                mcText(line, 1.85F);
            }
        }

        separatorText("Server Version");
        text("Protocol Version:");
        sameLine();
        text(String.valueOf(serverData.version));

        text("Game Version:");
        sameLine();
        textWrapped(serverData.gameVersion);

        if (treeNode("Player List:")) {
            if (serverData.playerList == null || serverData.playerList.isEmpty()) {
                text("Empty");
            } else {
                for (String player : serverData.playerList.split(", ")) {
                    text(player);
                }
            }
            treePop();
        }

        separator();

        if (button("Connect to Server")) {
            try {
                int defaultPort = 25565; // Default Minecraft port

                if (serverData.serverIP.contains(":")) {
                    String[] parts = serverData.serverIP.split(":");
                    String ip = parts[0];
                    int port = NumberUtils.toInt(parts[1], defaultPort);
                    FMLClientHandler.instance().connectToServerAtStartup(ip, port);
                } else {
                    FMLClientHandler.instance().connectToServerAtStartup(serverData.serverIP, defaultPort);
                }
            } catch (Exception e) {
                log.error("Failed to connect to server: {}", serverData.serverIP, e);
            }
        }
    }

    private ServerData getCurrentServer() {
        return Minecraft.getMinecraft().getCurrentServerData();
    }

    public static List<ServerData> getServerList() {
        Minecraft mc = Minecraft.getMinecraft();
        ServerList serverList = new ServerList(mc);
        serverList.loadServerList();

        List<ServerData> servers = new ArrayList<>();
        for (int i = 0; i < serverList.countServers(); i++) {
            servers.add(serverList.getServerData(i));
        }
        return servers;
    }

}
