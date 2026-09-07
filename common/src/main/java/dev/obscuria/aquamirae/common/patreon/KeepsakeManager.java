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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class KeepsakeManager {

    private static final String SOURCE;
    private static final Path CACHE_FILE;
    private static final Duration REQUEST_TIMEOUT;
    private static final ExecutorService EXECUTOR;
    private static final HttpClient HTTP_CLIENT;

    private static volatile KeepsakeRepository REPOSITORY = KeepsakeRepository.EMPTY;
    private static volatile KeepsakeArmor[] KEEPSAKES = new KeepsakeArmor[0];
    private static volatile CompletableFuture<Void> PENDING_RELOAD = CompletableFuture.completedFuture(null);

    public static ItemStack generateRandom(RandomSource randomSource, HolderGetter.Provider provider) {
        var keepsakes = KEEPSAKES;
        if (keepsakes.length == 0) return ItemStack.EMPTY;
        return keepsakes[randomSource.nextInt(keepsakes.length)].generateRandom(randomSource, provider);
    }

    public static synchronized CompletableFuture<Void> reload() {
        var next = PENDING_RELOAD
                .exceptionally(ignored -> null)
                .thenComposeAsync(ignored -> loadRepositoryAsync(), EXECUTOR)
                .thenAccept(repository -> {
                    REPOSITORY = repository;
                    KEEPSAKES = repository.keepsakes().values().toArray(KeepsakeArmor[]::new);
                }).exceptionally(exception -> {
                    Aquamirae.LOG.error("Unexpected failure while reloading keepsakes", exception);
                    return null;
                });

        PENDING_RELOAD = next;
        return next;
    }

    private static CompletableFuture<KeepsakeRepository> loadRepositoryAsync() {
        return fetchFromSourceAsync().handle((json, throwable) -> {
            if (throwable != null) {
                Aquamirae.LOG.warn("Failed to load keepsakes from source '{}'", SOURCE, throwable);
                return null;
            }
            return json;
        }).thenComposeAsync(json -> {
            if (json != null) {
                try {
                    var repository = KeepsakeRepository.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                    return saveToCacheAsync(json).thenApply(ignored -> repository);
                } catch (Exception exception) {
                    Aquamirae.LOG.warn("Failed to parse keepsakes from source '{}', will try cache", SOURCE, exception);
                }
            }
            return loadFromCacheRepositoryAsync();
        }, EXECUTOR);
    }

    private static CompletableFuture<KeepsakeRepository> loadFromCacheRepositoryAsync() {
        return loadFromCacheAsync().thenApply(json -> {
            if (json == null) {
                Aquamirae.LOG.warn("No keepsakes could be loaded from source or cache, using empty repository");
                return KeepsakeRepository.EMPTY;
            }
            try {
                return KeepsakeRepository.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            } catch (Exception exception) {
                Aquamirae.LOG.warn("Failed to parse cached keepsakes, falling back to empty", exception);
                return KeepsakeRepository.EMPTY;
            }
        });
    }

    private static CompletableFuture<JsonElement> fetchFromSourceAsync() {
        if (isRemote(SOURCE)) {
            var request = HttpRequest.newBuilder(URI.create(SOURCE)).timeout(REQUEST_TIMEOUT).GET().build();
            return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .orTimeout(REQUEST_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                    .thenApply(response -> {
                        if (response.statusCode() != 200) {
                            throw new CompletionException(new IOException("Unexpected HTTP status: " + response.statusCode()));
                        }
                        return JsonParser.parseString(response.body());
                    });
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return JsonParser.parseString(Files.readString(Path.of(SOURCE)));
            } catch (IOException exception) {
                throw new CompletionException(exception);
            }
        }, EXECUTOR);
    }

    private static boolean isRemote(String source) {
        return source.startsWith("https://");
    }

    private static CompletableFuture<@Nullable JsonElement> loadFromCacheAsync() {
        return CompletableFuture.supplyAsync(() -> {
            var pathToCache = Fragmentum.PLATFORM.getConfigDir().resolve(CACHE_FILE);
            try {
                if (!Files.exists(pathToCache)) return null;
                return JsonParser.parseString(Files.readString(pathToCache));
            } catch (Exception exception) {
                Aquamirae.LOG.warn("Failed to read keepsakes cache at '{}'", pathToCache, exception);
                return null;
            }
        }, EXECUTOR);
    }

    private static CompletableFuture<Void> saveToCacheAsync(JsonElement json) {
        return CompletableFuture.runAsync(() -> {
            var pathToCache = Fragmentum.PLATFORM.getConfigDir().resolve(CACHE_FILE);
            try {
                Files.createDirectories(pathToCache.getParent());
                Files.writeString(pathToCache, json.toString());
            } catch (IOException exception) {
                Aquamirae.LOG.warn("Failed to write keepsakes cache at '{}'", pathToCache, exception);
            }
        }, EXECUTOR);
    }

    static {
        SOURCE = "https://raw.githubusercontent.com/ObscuriaLithium/modding/refs/heads/main/patreon/aquamirae-keepsakes.json";
        CACHE_FILE = Path.of("obscuria/cache/aquamirae-keepsakes.json");
        REQUEST_TIMEOUT = Duration.ofSeconds(30);
        EXECUTOR = Executors.newCachedThreadPool(runnable -> {
            var thread = new Thread(runnable, "aquamirae-keepsake-loader");
            thread.setDaemon(true);
            return thread;
        });
        HTTP_CLIENT = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .executor(EXECUTOR)
                .build();
    }
}