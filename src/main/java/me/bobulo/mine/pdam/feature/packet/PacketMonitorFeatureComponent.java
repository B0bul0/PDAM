package me.bobulo.mine.pdam.feature.packet;

import me.bobulo.mine.pdam.feature.component.AbstractFeatureComponent;
import me.bobulo.mine.pdam.feature.component.ForgerListenerFeatureComponent;
import me.bobulo.mine.pdam.feature.packet.interceptor.PacketDataInterceptor;
import me.bobulo.mine.pdam.feature.packet.log.DisplayPacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.log.PacketLogEntry;
import me.bobulo.mine.pdam.feature.packet.gui.PacketLogGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketMonitorFeatureComponent extends AbstractFeatureComponent {

    public static PacketMonitorFeatureComponent INSTANCE; // TODO remove static access

    private static final int MAX_LOGS = 1_000_000;

    private int maxLogLimit = MAX_LOGS;

    private List<DisplayPacketLogEntry> packetEntries = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void onInit() {
        INSTANCE = this;

        addChildComponent(ForgerListenerFeatureComponent.of(
          new PacketDataInterceptor(this)
        ));
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
            packetEntries = new ArrayList<>(packetEntries.subList(packetEntries.size() - maxLogLimit, packetEntries.size()));
        }

        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen instanceof PacketLogGuiScreen) {
            ((PacketLogGuiScreen) currentScreen).refreshLogs();
        }
    }

    @NotNull
    public List<DisplayPacketLogEntry> getPacketEntries() {
        return packetEntries;
    }

}
