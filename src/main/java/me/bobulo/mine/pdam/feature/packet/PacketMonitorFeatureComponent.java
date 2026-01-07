package me.bobulo.mine.pdam.feature.packet;

import me.bobulo.mine.pdam.config.ConfigInitContext;
import me.bobulo.mine.pdam.feature.component.AbstractFeatureComponent;
import me.bobulo.mine.pdam.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.gui.PacketLogGuiScreen;
import me.bobulo.mine.pdam.feature.packet.interceptor.PacketDataInterceptor;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.util.BoundedConcurrentList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PacketMonitorFeatureComponent extends AbstractFeatureComponent {

    public static PacketMonitorFeatureComponent INSTANCE; // TODO remove static access

    private static final int MAX_LOGS = 2_000_000;

    private int maxLogLimit = MAX_LOGS;
    private boolean pauseLogging = false;

    private BoundedConcurrentList<DisplayPacketLogEntry> packetEntries;

    @Override
    protected void onInit() {
        INSTANCE = this;
        this.packetEntries = new BoundedConcurrentList<>(maxLogLimit);

        addChildComponent(ForgerListenerFeatureComponent.of(
          new PacketDataInterceptor(this)
        ));
    }

    @Override
    public void initProperties(ConfigInitContext context) {
        context.createProperty("maxLogLimit", MAX_LOGS)
          .comment("Maximum number of packet log entries to keep in memory (default: " + MAX_LOGS + ")")
          .max(MAX_LOGS)
          .min(1)
          .onUpdate(this::setMaxLogLimit);

        context.createProperty("pauseLogging", false)
          .comment("Pause packet logging (default: false)")
          .onUpdate(this::pauseLogging);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void addPacketEntry(@NotNull PacketLogEntry packetLogEntry) {
        if (isLoggingPaused()) {
            return;
        }

        DisplayPacketLogEntry display = DisplayPacketLogEntry.create(packetLogEntry);

        packetEntries.add(display);

        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen instanceof PacketLogGuiScreen) {
            ((PacketLogGuiScreen) currentScreen).refreshLogs();
        }
    }

    public void setMaxLogLimit(int maxLogLimit) {
        if (maxLogLimit < 1) {
            maxLogLimit = 1;
        } else if (maxLogLimit > MAX_LOGS) {
            maxLogLimit = MAX_LOGS;
        }

        this.maxLogLimit = maxLogLimit;

        // Recreate with new capacity
        BoundedConcurrentList<DisplayPacketLogEntry> newList = new BoundedConcurrentList<>(maxLogLimit);
        List<DisplayPacketLogEntry> snapshot = packetEntries.snapshot();

        // Copy recent entries
        int start = Math.max(0, snapshot.size() - maxLogLimit);
        for (int i = start; i < snapshot.size(); i++) {
            newList.add(snapshot.get(i));
        }

        this.packetEntries = newList;
    }

    public void pauseLogging(boolean pauseLogging) {
        this.pauseLogging = pauseLogging;
    }

    public boolean isLoggingPaused() {
        return pauseLogging;
    }

    @NotNull
    public List<DisplayPacketLogEntry> getPacketEntries() {
        return packetEntries;
    }

}
