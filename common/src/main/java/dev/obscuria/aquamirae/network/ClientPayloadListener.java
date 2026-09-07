package dev.obscuria.aquamirae.network;

import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.CameraShake;
import dev.obscuria.aquamirae.client.event.ShipbreakerClientState;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

final class ClientPayloadListener {

    public static void handleTestAction(Player player, ClientboundTestPayload payload) {
        var soundManager = Minecraft.getInstance().getSoundManager();
        if (payload.equals(ClientboundTestPayload.EARTHQUAKE)) {
            soundManager.play(SimpleSoundInstance.forLocalAmbience(AquamiraeSounds.EVENT_EARTHQUAKE, 1f, 1f));
            CameraShake.shake(0.5f, 4f);
        } else if (payload.equals(ClientboundTestPayload.MAZE_MOTHER_ENCOUNTER)) {
            soundManager.play(SimpleSoundInstance.forLocalAmbience(AquamiraeSounds.EVENT_GLACIER_CRACK, 1f, 1f));
            soundManager.play(SimpleSoundInstance.forLocalAmbience(AquamiraeSounds.ENTITY_MAZE_MOTHER_MOAN, 1f, 1f));
            CameraShake.shake(1f, 6f);
        }
    }

    public static void handleCameraShake(Player player, ClientboundCameraShakePayload payload) {
        CameraShake.shake(payload.strength(), payload.duration());
    }

    public static void handleAnimationPlay(Player player, ClientboundAnimationPlayPayload payload) {
        if (!(player.level().getEntity(payload.entityId()) instanceof IAnimatable animatable)) return;
        var state = animatable.getAnimationController().find(payload.animationName());
        if (state.isEmpty()) return;
        state.get().localPlay(payload.startTick(), payload.duration());
    }

    public static void handleAnimationStop(Player player, ClientboundAnimationStopPayload payload) {
        if (!(player.level().getEntity(payload.entityId()) instanceof IAnimatable animatable)) return;
        animatable.getAnimationController().find(payload.animationName()).ifPresent(AnimState::localStop);
    }

    public static void handleLogBookOpen(Player player, ClientboundLogBookOpenPayload payload) {
        AquamiraeClient.openLogbookScreen(payload.stack());
    }

    public static void handleTravel(Player player, ClientboundTravelPayload payload) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PORTAL_TRAVEL, 1f));
    }

    public static void handleShipbreakerAdd(Player player, ClientboundShipbreakerAddPayload payload) {
        ShipbreakerClientState.addEvent(payload.eventId(), payload.headIndex(), payload.path());
    }

    public static void handleShipbreakerRemove(Player player, ClientboundShipbreakerRemovePayload payload) {
        ShipbreakerClientState.removeEvent(payload.eventId());
    }
}