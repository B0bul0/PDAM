package me.bobulo.mine.pdam.feature.packet;

import me.bobulo.mine.pdam.config.ConfigInitContext;
import me.bobulo.mine.pdam.feature.component.AbstractFeatureComponent;
import me.bobulo.mine.pdam.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.gui.PacketLogGuiScreen;
import me.bobulo.mine.pdam.feature.packet.interceptor.PacketDataInterceptor;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

public class PacketMonitorFeatureComponent extends AbstractFeatureComponent {

    public static PacketMonitorFeatureComponent INSTANCE; // TODO remove static access

    private static final int MAX_LOGS = 1_000_000;

    private int maxLogLimit = MAX_LOGS;

    private List<DisplayPacketLogEntry> packetEntries = synchronizedList(new ArrayList<>());

    @Override
    protected void onInit() {
        INSTANCE = this;

        addChildComponent(ForgerListenerFeatureComponent.of(
          new PacketDataInterceptor(this)
        ));
    }

    @Override
    public void initProperties(ConfigInitContext context) {
        context.createProperty("maxLogLimit", MAX_LOGS)
          .comment("Maximum number of packet log entries to keep in memory (default: " + MAX_LOGS + ")")
          .onUpdate(this::setMaxLogLimit);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void addPacketEntry(@NotNull PacketLogEntry packetLogEntry) {
        DisplayPacketLogEntry display = DisplayPacketLogEntry.create(packetLogEntry);

        packetEntries.add(display);

        if (packetEntries.size() > maxLogLimit + (maxLogLimit / 3)) {
            packetEntries = synchronizedList(new ArrayList<>(packetEntries.subList(packetEntries.size() - maxLogLimit, packetEntries.size())));
        }

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
    }

    @NotNull
    public List<DisplayPacketLogEntry> getPacketEntries() {
        return packetEntries;
    }

}
