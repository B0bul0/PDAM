package me.bobulo.mine.pdam.feature.packet;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.feature.component.AbstractFeatureComponent;
import me.bobulo.mine.pdam.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.interceptor.PacketDataInterceptor;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.widow.PacketLogWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import org.jetbrains.annotations.NotNull;

public class PacketMonitorFeatureComponent extends AbstractFeatureComponent {

    private LogHistory<DisplayPacketLogEntry> packetEntries;
    private PacketLogWindow logWindow;

    @Override
    protected void onInit() {
        this.packetEntries = new LogHistory<>();

        addChildComponent(ForgerListenerFeatureComponent.of(
          new PacketDataInterceptor(this)
        ));

    }

    @Override
    protected void onEnable() {
        this.logWindow = new PacketLogWindow(packetEntries);
        PDAM.getImGuiRenderer().registerWidow(logWindow);
    }

    @Override
    protected void onDisable() {
        PDAM.getImGuiRenderer().unregisterWidow(logWindow);
        this.logWindow = null;

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
