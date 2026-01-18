package me.bobulo.mine.pdam.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;

public class GsonFileConfig implements PersistentConfig {

    private static final Gson GSON = new Gson();
    private static final Logger log = LogManager.getLogger(GsonFileConfig.class);

    private final File file;
    private JsonElement jsonElement;

    public GsonFileConfig(@NotNull File file) {
        this.file = file;
    }

    @Override
    public void loadConfig() {
        if (!file.exists()) {
            jsonElement = new JsonObject();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            JsonElement config = new JsonParser().parse(reader);
            if (config == null || !config.isJsonObject()) {
                jsonElement = new JsonObject();
            } else {
                jsonElement = config;
            }
        } catch (IOException e) {
            jsonElement = new JsonObject();
        }
    }

    @Override
    public void saveConfig() {
        if (jsonElement == null) { // not loaded
            return;
        }

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (Writer writer = new FileWriter(file)) {
            GSON.toJson(jsonElement, writer);
        } catch (IOException e) {
            // TODO launch exception
            log.error("Failed to save config to file: {}", file.getAbsolutePath(), e);
        }
    }

    @Override
    public String getString(String key) {
        JsonElement element = get(key);
        return element == null ? null : element.getAsString();
    }

    @Override
    public boolean getBoolean(String key) {
        JsonElement element = get(key);
        return element != null && element.getAsBoolean();
    }

    @Override
    public int getInt(String key) {
        JsonElement element = get(key);
        return element == null ? 0 : element.getAsInt();
    }

    @Override
    public long getLong(String key) {
        JsonElement element = get(key);
        return element == null ? 0L : element.getAsLong();
    }

    @Override
    public float getFloat(String key) {
        JsonElement element = get(key);
        return element == null ? 0.0f : element.getAsFloat();
    }

    @Override
    public double getDouble(String key) {
        JsonElement element = get(key);
        return element == null ? 0.0 : element.getAsDouble();
    }

    @Override
    public byte getByte(String key) {
        JsonElement element = get(key);
        return element == null ? 0 : element.getAsByte();
    }

    @Override
    public short getShort(String key) {
        JsonElement element = get(key);
        return element == null ? 0 : element.getAsShort();
    }

    @Override
    public <V> V getValue(String key, Class<V> valueType) {
        return GSON.fromJson(get(key), valueType);
    }

    @Override
    public <V> V getValue(String key, Type valueType) {
        return GSON.fromJson(get(key), valueType);
    }

    @Override
    public void setValue(@NotNull String key, Object value) {
        if (key.isEmpty()) {
            return;
        }

        if (value == null) {
            getJsonObject().remove(key);
            return;
        }

        if (value instanceof Number) {
            getJsonObject().addProperty(key, (Number) value);
        } else if (value instanceof String) {
            getJsonObject().addProperty(key, (String) value);
        } else if (value instanceof Boolean) {
            getJsonObject().addProperty(key, (Boolean) value);
        } else if (value instanceof Character) {
            getJsonObject().addProperty(key, (Character) value);
        } else {
            JsonElement element = GSON.toJsonTree(value);
            getJsonObject().add(key, element);
        }
    }

    private JsonElement get(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        return getJsonObject().get(key);
    }

    private JsonObject getJsonObject() {
        return jsonElement.getAsJsonObject();
    }

}
