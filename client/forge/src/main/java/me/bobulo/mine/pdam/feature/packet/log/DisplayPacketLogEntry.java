package me.bobulo.mine.pdam.feature.packet.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.log.LogEntry;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DisplayPacketLogEntry implements LogEntry {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
      .withZone(ZoneId.systemDefault());

    private static final Gson GSON = new GsonBuilder()
      .setPrettyPrinting()
      .serializeNulls()
      .create();

    public static DisplayPacketLogEntry create(PacketLogEntry packetLogEntry) {
        String json = GSON.toJson(packetLogEntry.getPacketData());

        String shortData = json.length() > 200 ? json.substring(0, 200) + "..." : json;
        shortData = shortData.replaceAll("\\s+", " ");

        return new DisplayPacketLogEntry(
          packetLogEntry,
          DATE_TIME_FORMATTER.format(packetLogEntry.getTimestamp()),
          packetLogEntry.getPacketName(),
          shortData,
          json,
          new String[]{
            packetLogEntry.getPacketName().toLowerCase(),
            packetLogEntry.getDirection().name().toLowerCase(),
            json.toLowerCase()
          }
        );
    }

    private final PacketLogEntry packetLogEntry;

    private final String formattedTime;
    private final String packetName;
    private final String packetDataShort;
    private final String packetData;
    private final String[] searchTerms;

    private DisplayPacketLogEntry(PacketLogEntry packetLogEntry, String formattedTime, String packetName, String packetDataShort, String packetData, String[] searchTerms) {
        this.packetLogEntry = packetLogEntry;
        this.formattedTime = formattedTime;
        this.packetName = packetName;
        this.packetDataShort = packetDataShort;
        this.packetData = packetData;
        this.searchTerms = searchTerms;
    }

    @Override
    public Instant getTimestamp() {
        return packetLogEntry.getTimestamp();
    }

    public PacketLogEntry getPacketLogEntry() {
        return packetLogEntry;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public String getPacketName() {
        return packetName;
    }

    public String getPacketData() {
        return packetData;
    }

    public String getPacketDataShort() {
        return packetDataShort;
    }

    public String[] getSearchTerms() {
        return searchTerms;
    }

    public PacketDirection getDirection() {
        return packetLogEntry.getDirection();
    }

}
