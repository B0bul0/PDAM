package me.bobulo.mine.pdam.feature.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.OldServerPinger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

public final class ServerScanner {

    private final OldServerPinger pinger = new OldServerPinger();
    private List<ServerData> serverCache = new ArrayList<>();
    private boolean isPinging = false;
    private final long timeoutMillis = 10_000L; // timeout de 10 segundos por ping

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
            // executor para cada ping (podemos usar cached para permitir paralelismo conforme necessário)
            ExecutorService executor = Executors.newCachedThreadPool();

            try {
                for (ServerData serverData : servers) {
                    if (!filter.test(serverData)) {
                        continue;
                    }

                    // marca visual antes de pingar
                    serverData.serverMOTD = "Pinging...";

                    // submit ping em task separada para aplicar timeout
                    Future<OldServerPinger> future = executor.submit(() -> {
                        OldServerPinger pinger = new OldServerPinger();
                        pinger.ping(serverData);
                        return pinger;
                    });

                    try {
                        future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException te) {
                        future.cancel(true);
                        serverData.serverMOTD = "Timeout";
                        serverData.pingToServer = -1L;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        serverData.serverMOTD = "Interrupted";
                        serverData.pingToServer = -1L;
                    } catch (ExecutionException ee) {
                        serverData.serverMOTD = "Error: " + (ee.getCause() != null ? ee.getCause().getLocalizedMessage() : ee.getLocalizedMessage());
                        serverData.pingToServer = -1L;
                    } catch (Exception e) {
                        serverData.serverMOTD = "Error: " + e.getLocalizedMessage();
                        serverData.pingToServer = -1L;
                    }
                }
            } finally {
                executor.shutdownNow();
                isPinging = false;
            }
        });
    }

    public List<ServerData> getServerCache() {
        return serverCache;
    }

    public boolean isPinging() {
        return isPinging;
    }

}