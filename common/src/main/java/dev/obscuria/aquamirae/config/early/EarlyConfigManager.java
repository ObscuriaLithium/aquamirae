package dev.obscuria.aquamirae.config.early;

import com.google.gson.*;
import dev.obscuria.fragmentum.Fragmentum;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class EarlyConfigManager {

    public static final EarlyConfigManager SHARED = new EarlyConfigManager();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configPath = Fragmentum.PLATFORM.getConfigDir().resolve("obscuria/aquamirae-early.json");
    private JsonObject cache = new JsonObject();

    public void load() {
        if (!Files.exists(configPath)) {
            cache = new JsonObject();
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (!element.isJsonObject()) {
                throw new IllegalStateException("Early config root must be a JSON object");
            }

            cache = element.getAsJsonObject();
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException(
                    "Failed to load early config from " + configPath, e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                GSON.toJson(cache, writer);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save early config to " + configPath, e);
        }
    }

    public boolean has(String key) {
        return cache.has(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        JsonElement value = cache.get(key);
        return value != null && value.isJsonPrimitive()
                ? value.getAsBoolean()
                : defaultValue;
    }

    public void setBoolean(String key, boolean value) {
        cache.addProperty(key, value);
    }

    public int getInt(String key, int defaultValue) {
        JsonElement value = cache.get(key);
        return value != null && value.isJsonPrimitive()
                ? value.getAsInt()
                : defaultValue;
    }

    public void setInt(String key, int value) {
        cache.addProperty(key, value);
    }

    public long getLong(String key, long defaultValue) {
        JsonElement value = cache.get(key);
        return value != null && value.isJsonPrimitive()
                ? value.getAsLong()
                : defaultValue;
    }

    public void setLong(String key, long value) {
        cache.addProperty(key, value);
    }

    public float getFloat(String key, float defaultValue) {
        JsonElement value = cache.get(key);
        return value != null && value.isJsonPrimitive()
                ? value.getAsFloat()
                : defaultValue;
    }

    public void setFloat(String key, float value) {
        cache.addProperty(key, value);
    }

    public double getDouble(String key, double defaultValue) {
        JsonElement value = cache.get(key);
        return value != null && value.isJsonPrimitive()
                ? value.getAsDouble()
                : defaultValue;
    }

    public void setDouble(String key, double value) {
        cache.addProperty(key, value);
    }

    public String getString(String key, String defaultValue) {
        JsonElement value = cache.get(key);
        return value != null && value.isJsonPrimitive()
                ? value.getAsString()
                : defaultValue;
    }

    public void setString(String key, String value) {
        cache.addProperty(key, value);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}