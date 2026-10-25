package me.bobulo.mine.pdam.feature.packet;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.ToolbarMenuImGuiRender;
import me.bobulo.mine.pdam.feature.packet.interceptor.PacketDataInterceptor;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.window.PacketLogWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class PacketMonitorFeature extends AbstractFeature {

    public static final String FEATURE_ID = "packet_monitor";

    private LogHistory<DisplayPacketLogEntry> packetEntries;

    private PacketRateTracker rateTrackerServer;
    private PacketRateTracker rateTrackerClient;

    private PacketLogWindow logWindow;
    private ForgeListenerBinding listenerBinding;
    private ImGuiListenerBinding windowBinding;

    public PacketMonitorFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        this.packetEntries = new LogHistory<>();
        this.rateTrackerServer = new PacketRateTracker(1, TimeUnit.SECONDS);
        this.rateTrackerClient = new PacketRateTracker(1, TimeUnit.SECONDS);

        this.logWindow = new PacketLogWindow(packetEntries, rateTrackerServer, rateTrackerClient);
        ToolbarMenuImGuiRender menu = new ToolbarMenuImGuiRender(ImmutableList.of(logWindow));
        context.getScope().registerBehavior(menu);
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        listenerBinding = new ForgeListenerBinding(new PacketDataInterceptor(this));
        windowBinding = new ImGuiListenerBinding(logWindow);
        context.getScope().registerBindings(listenerBinding, windowBinding);
    }

    @Override
    protected void onDisable(@NotNull FeatureDisableContext context) {
        try {
            if (context.getScope().getBindings().contains(listenerBinding)) {
                context.getScope().unregisterBinding(listenerBinding);
            }
            if (context.getScope().getBindings().contains(windowBinding)) {
                context.getScope().unregisterBinding(windowBinding);
            }
        } finally {
            listenerBinding = null;
            windowBinding = null;
            packetEntries.clear();
        }
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
