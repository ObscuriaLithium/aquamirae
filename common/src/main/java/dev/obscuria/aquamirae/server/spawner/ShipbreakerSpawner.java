package dev.obscuria.aquamirae.server.spawner;

import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.network.ClientboundTestPayload;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBiomeTags;
import dev.obscuria.aquamirae.server.event.ShipbreakerEvent;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.CustomSpawner;

import java.util.ArrayList;
import java.util.Collections;

public final class ShipbreakerSpawner implements CustomSpawner {

    private static final int MIN_COOLDOWN = 600;
    private static final int TICK_IN_MINUTE = 1200;

    private int cooldown = pickCooldown(RandomSource.create());

    @Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!CommonConfig.ENABLE_SHIPBREAKER_SPAWNING.get()) return 0;
        if (--cooldown > 0) return 0;
        this.cooldown = trySpawn(level)
                ? pickCooldown(level.getRandom())
                : pickRetryDelay(level.getRandom());
        return 0;
    }

    private boolean trySpawn(ServerLevel level) {
        if (level.players().isEmpty()) return false;
        var players = new ArrayList<>(level.players());
        Collections.shuffle(players);
        for (var player : players) {
            if (!level.getBiome(player.blockPosition()).is(AquamiraeBiomeTags.SPAWNS_SHIPBREAKER)) continue;
            ShipbreakerEvent.trigger(level, player.position());
            FragmentumNetworking.sendTo(player, ClientboundTestPayload.EARTHQUAKE);
            return true;
        }
        return false;
    }

    private static int pickCooldown(RandomSource random) {
        var baseCooldown = minutesToTicks(CommonConfig.SHIPBREAKER_SPAWN_COOLDOWN.get());
        var variance = minutesToTicks(CommonConfig.SHIPBREAKER_SPAWN_COOLDOWN_VARIANCE.get());
        return Math.max(MIN_COOLDOWN, variance > 0 ? baseCooldown + (int) random.triangle(0, variance) : baseCooldown);
    }

    private static int pickRetryDelay(RandomSource random) {
        return minutesToTicks(CommonConfig.SHIPBREAKER_SPAWN_RETRY_DELAY.get());
    }

    private static int minutesToTicks(double minutes) {
        return (int) Math.round(minutes * TICK_IN_MINUTE);
    }
}