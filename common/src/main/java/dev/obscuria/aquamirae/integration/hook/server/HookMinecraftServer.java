package dev.obscuria.aquamirae.integration.hook.server;

import dev.obscuria.aquamirae.common.patreon.KeepsakeManager;
import dev.obscuria.aquamirae.server.spawner.ShipbreakerSpawner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public final class HookMinecraftServer {

    public static void onRunServer(MinecraftServer server) {
        KeepsakeManager.reload();
    }

    public static void addCustomSpawners(ResourceKey<Level> dimension, Consumer<CustomSpawner> consumer) {
        if (dimension != Level.OVERWORLD) return;
        consumer.accept(new ShipbreakerSpawner());
    }

    public static void onStopServer(MinecraftServer server) {}
}
