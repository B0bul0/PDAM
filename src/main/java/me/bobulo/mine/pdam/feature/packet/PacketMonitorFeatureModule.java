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

public final class PacketMonitorFeatureModule extends AbstractFeatureModule {

    private final PacketMonitor packetMonitor;
    private LogHistory<DisplayPacketLogEntry> packetEntries;
    private PacketLogWindow logWindow;
    private ToolbarMenuImGuiRender toolbarMenuImGuiRenderer;

    public PacketMonitorFeatureModule(PacketMonitor packetMonitor) {
        this.packetMonitor = packetMonitor;
    }

    @Override
    protected void onInitialize() {
        this.packetEntries = new LogHistory<>();

        getFeature().addModule(ForgerListenerFeatureModule.of(
          new PacketDataInterceptor(this)
        ));

        this.logWindow = new PacketLogWindow(packetEntries);
        getFeature().addModule(ImGuiListenerFeatureModule.of(logWindow));

        getFeature().addModule(new ToolbarMenuImGuiRender(ImmutableList.of(logWindow)));
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {
        this.packetEntries.clear();
    }

    public void addPacketEntry(@NotNull PacketLogEntry packetLogEntry) {
        if (packetEntries.isPaused()) {
            return;
        }

        DisplayPacketLogEntry display = DisplayPacketLogEntry.create(packetLogEntry);

        packetEntries.addLogEntry(display);
    }

}
