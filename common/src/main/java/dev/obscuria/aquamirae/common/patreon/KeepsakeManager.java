package dev.obscuria.aquamirae.common.patreon;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.fragmentum.Fragmentum;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

public final class KeepsakeManager {

    private static final String SOURCE = "https://raw.githubusercontent.com/ObscuriaLithium/modding/refs/heads/main/patreon/aquamirae-keepsakes.json";

    private static final Path CACHE_FILE = Path.of("obscuria/cache/aquamirae-keepsakes.json");
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();

    private static volatile KeepsakeRepository REPOSITORY = KeepsakeRepository.EMPTY;
    private static volatile KeepsakeArmor[] KEEPSAKES = new KeepsakeArmor[0];

    public static ItemStack generateRandom(RandomSource randomSource, HolderGetter.Provider provider) {
        var keepsakes = KEEPSAKES;
        if (keepsakes.length == 0) return ItemStack.EMPTY;
        return keepsakes[randomSource.nextInt(keepsakes.length)].generateRandom(randomSource, provider);
    }

    public static void reload() {
        var repository = loadRepository();
        REPOSITORY = repository;
        KEEPSAKES = repository.keepsakes().values().toArray(KeepsakeArmor[]::new);
    }

    private static KeepsakeRepository loadRepository() {
        @Nullable var json = fetchFromSourceWithTimeout();

        if (json != null) {
            try {
                var repository = KeepsakeRepository.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                saveToCache(json);
                return repository;
            } catch (Exception exception) {
                Aquamirae.LOG.warn("Failed to parse keepsakes from source '{}', will try cache", SOURCE, exception);
            }
        }

        json = loadFromCache();
        if (json != null) {
            try {
                return KeepsakeRepository.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            } catch (Exception exception) {
                Aquamirae.LOG.warn("Failed to parse cached keepsakes, falling back to empty", exception);
            }
        }

        Aquamirae.LOG.warn("No keepsakes could be loaded from source or cache, using empty repository");
        return KeepsakeRepository.EMPTY;
    }

    @Nullable
    private static JsonElement fetchFromSourceWithTimeout() {
        var future = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchFromSource();
            } catch (Exception exception) {
                throw new CompletionException(exception);
            }
        });

        try {
            return future.get(REQUEST_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        } catch (Exception exception) {
            Aquamirae.LOG.warn("Failed to load keepsakes from source '{}'", SOURCE, exception);
            return null;
        }
    }

    private static JsonElement fetchFromSource() throws Exception {
        if (isRemote(SOURCE)) {
            var request = HttpRequest.newBuilder(URI.create(SOURCE)).timeout(REQUEST_TIMEOUT).GET().build();
            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected HTTP status: " + response.statusCode());
            }
            return JsonParser.parseString(response.body());
        }

        return JsonParser.parseString(Files.readString(Path.of(SOURCE)));
    }

    private static boolean isRemote(String source) {
        return source.startsWith("https://");
    }

    @Nullable
    private static JsonElement loadFromCache() {
        var pathToCache = Fragmentum.PLATFORM.getConfigDir().resolve(CACHE_FILE);
        try {
            if (!Files.exists(pathToCache)) return null;
            return JsonParser.parseString(Files.readString(pathToCache));
        } catch (Exception exception) {
            Aquamirae.LOG.warn("Failed to read keepsakes cache at '{}'", pathToCache, exception);
            return null;
        }
    }

    private static void saveToCache(JsonElement json) {
        var pathToCache = Fragmentum.PLATFORM.getConfigDir().resolve(CACHE_FILE);
        try {
            Files.createDirectories(pathToCache.getParent());
            Files.writeString(pathToCache, json.toString());
        } catch (IOException exception) {
            Aquamirae.LOG.warn("Failed to write keepsakes cache at '{}'", pathToCache, exception);
        }
    }
}