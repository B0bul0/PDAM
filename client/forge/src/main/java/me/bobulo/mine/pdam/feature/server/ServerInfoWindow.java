package me.bobulo.mine.pdam.feature.server;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.ServerConnector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;
import static me.bobulo.mine.pdam.imgui.util.MCFontImGui.mcText;

public class ServerInfoWindow extends AbstractRenderItemWindow {

    private static final Logger log = LogManager.getLogger(ServerInfoWindow.class);
    private final ServerScanner serverScanner = new ServerScanner();
    private final ImGuiTextFilter serverFilter = new ImGuiTextFilter();

    private final ImBoolean rawMOTD = new ImBoolean(true);
    private final ImString motd = new ImString();

    private final ImBoolean firstPing = new ImBoolean(true);

    public ServerInfoWindow() {
        super("Server Info");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(380, 550, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Server Info##ServerInfoWindow", isVisible)) {
            keepInScreen();
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
                }
                endChild(); // tem que virar fora, corrigido
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

        if (firstPing.get() && !serverScanner.isPinging()) {
            serverScanner.pingServerList();
            firstPing.set(false);
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

        if (!isConnected(serverData)) {
            if (button("Connect to Server")) {
                try {
                    ServerConnector.connectToServer(serverData);
                } catch (Exception e) {
                    log.error("Failed to connect to server: {}", serverData.serverIP, e);
                }
            }
        }
    }

    private boolean isConnected(ServerData serverData) {
        ServerData currentServer = getCurrentServer();
        if (currentServer == null || serverData == null) {
            return false;
        }

        return currentServer.serverIP.equals(serverData.serverIP);
    }

    private ServerData getCurrentServer() {
        return Minecraft.getMinecraft().getCurrentServerData();
    }

}
