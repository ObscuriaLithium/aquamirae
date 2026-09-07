package dev.obscuria.aquamirae.server.event;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.api.server.TickableEvent;
import dev.obscuria.aquamirae.api.server.ServerLevelHelper;
import dev.obscuria.aquamirae.common.event.ShipbreakerPath;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.network.ClientboundShipbreakerAddPayload;
import dev.obscuria.aquamirae.network.ClientboundShipbreakerRemovePayload;
import dev.obscuria.aquamirae.registry.AquamiraeCriteriaTriggers;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBlockTags;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public final class ShipbreakerEvent implements TickableEvent {

    public static final ResourceKey<DamageType> SHIPBREAKER = ResourceKey.create(Registries.DAMAGE_TYPE, Aquamirae.id("shipbreaker"));

    public static final double SPEED_BLOCKS_PER_TICK = 0.6;
    public static final double HEAD_KILL_RADIUS = 11.0;
    public static final double BODY_PUSH_RADIUS = 11.0;
    public static final double PUSH_FORCE = 1.4;

    private static final int BODY_CHECK_INTERVAL = 3;
    private static final int PHYSICS_TICK_INTERVAL = 2;
    private static final double TRACKING_RADIUS = 512.0;
    private static final double TRACKING_RADIUS_SQR = TRACKING_RADIUS * TRACKING_RADIUS;

    public final UUID id;

    private final ShipbreakerPath path;
    private double headIndex = 0.0;
    private int ticksSinceLastPhysics = 0;
    private final Set<ServerPlayer> players;
    private final Set<UUID> invulnerableEntities;

    private ShipbreakerEvent(UUID id, ShipbreakerPath path) {
        this.path = path;
        this.id = id;
        this.players = new HashSet<>();
        this.invulnerableEntities = new HashSet<>();
    }

    public static ShipbreakerEvent trigger(ServerLevel level, Vec3 playerPos) {
        var path = ShipbreakerPath.buildAround(level, playerPos);
        var event = new ShipbreakerEvent(UUID.randomUUID(), path);
        ServerLevelHelper.triggerEvent(level, event);
        event.updateTrackedPlayers(level);
        return event;
    }

    public boolean isFinished() {
        return headIndex >= path.size();
    }

    public void tick(ServerLevel level) {
        headIndex += SPEED_BLOCKS_PER_TICK;
        updateTrackedPlayers(level);
        if (isFinished()) return;

        ticksSinceLastPhysics++;
        if (ticksSinceLastPhysics >= PHYSICS_TICK_INTERVAL) {
            ticksSinceLastPhysics = 0;
            applyPhysics(level);
        }
    }

    @Override
    public void onStop(ServerLevel level) {
        untrackAllPlayers();
    }

    private void updateTrackedPlayers(ServerLevel level) {
        var headIdx = Math.min((int) headIndex, path.size() - 1);
        var headPos = path.points().get(headIdx);

        for (var player : Set.copyOf(players)) {
            if (player.level() == level && shouldTrack(headPos, player)) continue;
            untrackPlayer(player);
        }

        for (var player : level.players()) {
            if (!shouldTrack(headPos, player)) continue;
            trackPlayer(player);
        }
    }

    private void trackPlayer(ServerPlayer player) {
        if (!players.add(player)) return;
        var payload = new ClientboundShipbreakerAddPayload(id, headIndex, path.points());
        FragmentumNetworking.sendTo(player, payload);
    }

    private void untrackPlayer(ServerPlayer player) {
        if (!players.remove(player)) return;
        var payload = new ClientboundShipbreakerRemovePayload(id);
        FragmentumNetworking.sendTo(player, payload);
    }

    private void untrackAllPlayers() {
        for (var player : Set.copyOf(players)) {
            untrackPlayer(player);
        }
    }

    private boolean shouldTrack(Vec3 headPos, ServerPlayer player) {
        return player.position().distanceToSqr(headPos) <= TRACKING_RADIUS_SQR;
    }

    private void applyPhysics(ServerLevel level) {
        int headIdx = (int) headIndex;
        Vec3 headPos = path.points().get(headIdx);

        killEntitiesNear(level, headPos, HEAD_KILL_RADIUS);

        var headBreakIndex = headIdx + 2;
        if (path.points().size() > headBreakIndex) {
            var breakPos = path.points().get(headBreakIndex);
            breakBlocksNear(level, breakPos, HEAD_KILL_RADIUS);
        }

        int tailIdx = Math.max(0, headIdx - ShipbreakerPath.BODY_LENGTH);
        for (int i = headIdx; i >= tailIdx; i -= BODY_CHECK_INTERVAL) {
            var segPos = path.points().get(i);
            this.pushEntitiesNear(level, segPos, BODY_PUSH_RADIUS);
        }

        for (var player : level.getPlayers(this::isNearby)) {
            AquamiraeCriteriaTriggers.SHIPBREAKER_ENCOUNTER.trigger(player);
        }
    }

    private boolean isNearby(ServerPlayer player) {
        int headIdx = (int) headIndex;
        var headPos = path.points().get(headIdx);
        return player.position().distanceTo(headPos) <= 96;
    }

    private void breakBlocksNear(ServerLevel level, Vec3 center, double radius) {
        if (!CommonConfig.ENABLE_SHIPBREAKER_GRIEFING.get()) return;
        var intRadius = Mth.ceil(radius);
        var radiusSqr = radius * radius;
        var centerPos = BlockPos.containing(center);
        var pos = new BlockPos.MutableBlockPos();

        for (var x = -intRadius; x <= intRadius; x++) {
            for (var y = -intRadius; y <= intRadius; y++) {
                for (var z = -intRadius; z <= intRadius; z++) {
                    var distanceSqr = x * x + y * y + z * z;
                    if (distanceSqr > radiusSqr) continue;
                    pos.set(centerPos).move(x, y, z);
                    if (!level.getBlockState(pos).is(AquamiraeBlockTags.SHIPBREAKER_BREAKABLES)) continue;
                    level.destroyBlock(pos, false);
                }
            }
        }
    }

    private void killEntitiesNear(ServerLevel level, Vec3 center, double radius) {
        var source = level.damageSources().source(SHIPBREAKER);
        for (var entity : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(center, center).inflate(radius),
                entityDistanceCheck(center, radius))) {

            if (invulnerableEntities.contains(entity.getUUID())) continue;
            entity.hurt(source, Float.MAX_VALUE);
            if (!(entity instanceof ServerPlayer player)) continue;
            if (player.isCreative() || player.isSpectator()) continue;
            if (player.isDeadOrDying()) continue;
            this.invulnerableEntities.add(entity.getUUID());
            this.playerSurvived(player);
        }
    }

    private void pushEntitiesNear(ServerLevel level, Vec3 center, double radius) {
        for (var entity : level.getEntities((Entity) null,
                new AABB(center, center).inflate(radius),
                entityDistanceCheck(center, radius))) {

            Vec3 ePos = entity.position();
            double dx = ePos.x - center.x;
            double dy = ePos.y - center.y;
            double dz = ePos.z - center.z;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist < 0.01) continue;

            double falloff = 1.0 - (dist / radius);
            double force = PUSH_FORCE * falloff;

            entity.addDeltaMovement(new Vec3(
                    dx / dist * force,
                    dy / dist * force + 0.3 * falloff,
                    dz / dist * force));
            entity.hurtMarked = true;
        }
    }

    private Predicate<Entity> entityDistanceCheck(Vec3 center, double radius) {
        return entity -> entity.distanceToSqr(center.x, center.y, center.z) <= radius * radius;
    }

    private void playerSurvived(ServerPlayer player) {
        AquamiraeCriteriaTriggers.SHIPBREAKER_SURVIVAL.trigger(player);
        var stack = AquamiraeItems.MAELSTROM_VESTIGE.instantiate();
        if (player.addItem(stack)) return;
        player.drop(stack, true);
    }
}
