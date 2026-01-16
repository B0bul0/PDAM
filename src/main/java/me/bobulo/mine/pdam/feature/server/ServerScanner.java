package me.bobulo.mine.pdam.feature.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.OldServerPinger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public final class ServerScanner {

    private final OldServerPinger pinger = new OldServerPinger();
    private List<ServerData> serverCache = new ArrayList<>();
    private boolean isPinging = false;

    public ServerScanner() {
        refreshServerList();
    }

    public void refreshServerList() {
        Minecraft mc = Minecraft.getMinecraft();
        ServerList list = new ServerList(mc);
        list.loadServerList();

        serverCache.clear();

        for (int i = 0; i < list.countServers(); i++) {
            ServerData data = list.getServerData(i);
            serverCache.add(data);
        }
    }

    public void pingServerList() {
        pingServerList(serverData -> true);
    }

    public void pingServerList(@NotNull Predicate<ServerData> filter) {
        if (isPinging) { // prevent concurrent pings
            return;
        }

        isPinging = true;

        List<ServerData> servers = new ArrayList<>(this.serverCache); // create a copy to avoid concurrent modification

        CompletableFuture.runAsync(() -> {
              for (ServerData serverData : servers) {
                  if (!filter.test(serverData)) {
                      continue;
                  }

                  try {
                      serverData.serverMOTD = "§7Pinging...";
                      pinger.ping(serverData);
                  } catch (Exception e) {
                      serverData.serverMOTD = "§cError: " + e.getLocalizedMessage();
                      serverData.pingToServer = -1L;
                  }
              }
          })
          .whenComplete((result, throwable) -> isPinging = false);
    }

    public List<ServerData> getServerCache() {
        return serverCache;
    }

    public boolean isPinging() {
        return isPinging;
    }

}