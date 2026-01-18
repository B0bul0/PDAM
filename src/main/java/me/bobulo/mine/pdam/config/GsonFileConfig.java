package me.bobulo.mine.pdam.config;

import com.google.gson.*;
import me.bobulo.mine.pdam.config.exception.ConfigLoadException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;

public class GsonFileConfig implements PersistentConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
        } catch (Exception e) {
            throw new ConfigLoadException("Failed to load config from file: " + file.getAbsolutePath(), e);
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to save config to file: " + file.getAbsolutePath(), e);
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

        String[] parts = splitKey(key);
        JsonObject parent = getOrCreateParent(parts);
        String lastKey = parts[parts.length - 1];

        if (value == null) {
            parent.remove(lastKey);
            return;
        }

        if (value instanceof Number) {
            parent.addProperty(lastKey, (Number) value);
        } else if (value instanceof String) {
            parent.addProperty(lastKey, (String) value);
        } else if (value instanceof Boolean) {
            parent.addProperty(lastKey, (Boolean) value);
        } else if (value instanceof Character) {
            parent.addProperty(lastKey, (Character) value);
        } else {
            JsonElement element = GSON.toJsonTree(value);
            parent.add(lastKey, element);
        }
    }

    private JsonElement get(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        return getByPath(key);
    }

    private JsonElement getByPath(String key) {
        String[] parts = splitKey(key);
        JsonElement current = ensureRoot();

        for (String part : parts) {
            if (current == null || !current.isJsonObject()) {
                return null;
            }
            current = current.getAsJsonObject().get(part);
        }

        return current;
    }

    private JsonObject getOrCreateParent(String[] parts) {
        JsonObject root = ensureRoot().getAsJsonObject();
        if (parts.length == 1) {
            return root;
        }

        JsonObject current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            JsonElement child = current.get(part);

            if (child == null || !child.isJsonObject()) {
                JsonObject obj = new JsonObject();
                current.add(part, obj);
                current = obj;
            } else {
                current = child.getAsJsonObject();
            }
        }

        return current;
    }

    private JsonElement ensureRoot() {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            jsonElement = new JsonObject();
        }

        return jsonElement;
    }

    private String[] splitKey(String key) {
        if (key == null) {
            return new String[0];
        }

        return Arrays.stream(key.split("\\."))
          .filter(s -> !s.isEmpty())
          .toArray(String[]::new);
    }

}
