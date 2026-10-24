package me.bobulo.mine.pdam.feature.packet;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRender;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleAttachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
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
    private ToolbarMenuImGuiRender menu;
    private ForgeListenerBinding listenerBinding;
    private ImGuiListenerBinding windowBinding;

    @Override
    protected void onInitialize(@NotNull FeatureModuleAttachContext context) {
        this.packetEntries = new LogHistory<>();
        this.rateTrackerServer = new PacketRateTracker(1, TimeUnit.SECONDS);
        this.rateTrackerClient = new PacketRateTracker(1, TimeUnit.SECONDS);

        this.logWindow = new PacketLogWindow(packetEntries, rateTrackerServer, rateTrackerClient);
        this.menu = new ToolbarMenuImGuiRender(ImmutableList.of(logWindow));
        context.getScope().registerBehavior(menu);
    }

    @Override
    protected void onEnable(@NotNull FeatureModuleEnableContext context) {
        listenerBinding = new ForgeListenerBinding(new PacketDataInterceptor(this));
        windowBinding = new ImGuiListenerBinding(logWindow);
        context.getScope().registerBindings(listenerBinding, windowBinding);
    }

    @Override
    protected void onUninitialize(@NotNull FeatureModuleDetachContext context) {
        context.getScope().unregisterBehavior(menu);
    }

    @Override
    protected void onDisable(@NotNull FeatureModuleDisableContext context) {
        context.getScope().unregisterBinding(listenerBinding);
        context.getScope().unregisterBinding(windowBinding);
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
