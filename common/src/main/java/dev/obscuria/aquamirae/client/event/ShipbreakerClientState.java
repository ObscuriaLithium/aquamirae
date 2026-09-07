package dev.obscuria.aquamirae.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.client.CameraShake;
import dev.obscuria.aquamirae.client.renderer.spline.ShipbreakerRenderer;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.common.event.ShipbreakerBody;
import dev.obscuria.aquamirae.common.event.ShipbreakerPath;
import dev.obscuria.aquamirae.server.event.ShipbreakerEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ShipbreakerClientState {

    private static final List<ActiveClientEvent> ACTIVE_EVENTS = new ArrayList<>();
    private static final ShipbreakerRenderer SHARED_RENDERER = new ShipbreakerRenderer();

    public static void addEvent(UUID eventId, double headIndex, List<Vec3> points) {
        ACTIVE_EVENTS.removeIf(event -> event.eventId.equals(eventId));
        ACTIVE_EVENTS.add(new ActiveClientEvent(eventId, headIndex, points));
    }

    public static void removeEvent(UUID eventId) {
        ACTIVE_EVENTS.removeIf(event -> event.eventId.equals(eventId));
    }

    public static void tick(ClientLevel level) {
        for (var event : ACTIVE_EVENTS)
            event.tick(level);
    }

    public static void render(
            PoseStack pose, ClientLevel level, Frustum frustum, Camera camera,
            MultiBufferSource.BufferSource source, float partialTick
    ) {
        for (var event : ACTIVE_EVENTS)
            event.render(pose, level, frustum, camera, source, partialTick);
    }

    private static final class ActiveClientEvent {

        private final UUID eventId;
        private final ShipbreakerBody body;
        private int tickCount = 0;
        private double headIndex;

        ActiveClientEvent(UUID eventId, double headIndex, List<Vec3> path) {
            this.eventId = eventId;
            this.headIndex = headIndex;
            this.body = new ShipbreakerBody(ShipbreakerPath.BODY_LENGTH, 8, 10f, 12f);
            this.body.setPath(new ArrayList<>(path));
        }

        void tick(ClientLevel level) {
            tickCount++;

            headIndex += ShipbreakerEvent.SPEED_BLOCKS_PER_TICK;

            body.setHeadIndex(headIndex);
            body.tick();

            Vec3 headPos = body.path.get(Math.min(body.headIndex(), body.path.size() - 1));
            @Nullable var player = Minecraft.getInstance().player;
            if (player == null) return;

            double distance = headPos.distanceTo(player.position());
            if (distance > 128) return;

            float factor = 1F - (float) distance / 128F;
            CameraShake.shake(Mth.lerp(factor, 0F, 2F), 1F);

            if (tickCount % 40 == 0) {
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forLocalAmbience(
                                level.random.nextFloat() < factor
                                        ? AquamiraeSounds.EVENT_EARTHQUAKE
                                        : AquamiraeSounds.EVENT_GLACIER_CRACK,
                                factor, factor * 2f));
                if (level.random.nextFloat() * 4f < factor) {
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forLocalAmbience(
                                    AquamiraeSounds.ENTITY_MAZE_MOTHER_MOAN,
                                    Mth.lerp(factor, 0.6f, 0.9f), factor * 2f));
                }
            }
        }

        void render(
                PoseStack pose, ClientLevel level, Frustum frustum, Camera camera,
                MultiBufferSource.BufferSource source, float partialTick
        ) {
            SHARED_RENDERER.render(pose, body, level, frustum, camera, source, partialTick);
        }
    }
}
