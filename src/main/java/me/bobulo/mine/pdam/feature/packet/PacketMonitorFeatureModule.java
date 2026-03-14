package me.bobulo.mine.pdam.feature.packet;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.ForgerListenerFeatureModule;
import me.bobulo.mine.pdam.feature.module.ImGuiListenerFeatureModule;
import me.bobulo.mine.pdam.feature.packet.interceptor.PacketDataInterceptor;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.window.PacketLogWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class PacketMonitorFeatureModule extends AbstractFeatureModule {

    private LogHistory<DisplayPacketLogEntry> packetEntries;

    private PacketRateTracker rateTrackerServer;
    private PacketRateTracker rateTrackerClient;

    private PacketLogWindow logWindow;

    @Override
    protected void onInitialize() {
        this.packetEntries = new LogHistory<>();
        this.rateTrackerServer = new PacketRateTracker(1, TimeUnit.SECONDS);
        this.rateTrackerClient = new PacketRateTracker(1, TimeUnit.SECONDS);

        getFeature().addModule(ForgerListenerFeatureModule.of(
          new PacketDataInterceptor(this)
        ));

        this.logWindow = new PacketLogWindow(packetEntries, rateTrackerServer, rateTrackerClient);
        getFeature().addModule(ImGuiListenerFeatureModule.of(logWindow));

        getFeature().addModule(new ToolbarMenuImGuiRender(ImmutableList.of(logWindow)));
    }

    @Override
    protected void onDisable() {
        this.packetEntries.clear();
    }

    public void addPacketEntry(@NotNull PacketLogEntry packetLogEntry) {
        if (packetLogEntry.getDirection() == PacketDirection.SERVER) {
            rateTrackerServer.count();
        } else {
            rateTrackerClient.count();
        }

        if (packetEntries.isPaused()) {
            return;
        }

        DisplayPacketLogEntry display = DisplayPacketLogEntry.create(packetLogEntry);

        packetEntries.addLogEntry(display);
    }

}
