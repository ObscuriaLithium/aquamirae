package dev.obscuria.aquamirae.common.entity.animation;

import dev.obscuria.aquamirae.network.ClientboundAnimationPlayPayload;
import dev.obscuria.aquamirae.network.ClientboundAnimationStopPayload;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import lombok.Getter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public final class AnimState {

    @Getter private final String name;
    @Getter private final int defaultDuration;
    @Getter private int tick;
    @Getter private int duration;

    public AnimState(String name, int defaultDuration) {
        this.defaultDuration = defaultDuration;
        this.name = name;
    }

    public void play(Entity entity) {
        play(entity, defaultDuration);
    }

    public void play(Entity entity, int duration) {
        play(entity, 0, duration);
    }

    public void play(Entity entity, int startTick, int duration) {
        if (entity.level().isClientSide()) return;
        localPlay(startTick, duration);
        var payload = new ClientboundAnimationPlayPayload(entity.getId(), name, startTick, duration);
        FragmentumNetworking.sendToAllTracking(entity, payload);
    }

    public void stop(Entity entity) {
        if (entity.level().isClientSide()) return;
        localStop();
        var payload = new ClientboundAnimationStopPayload(entity.getId(), name);
        FragmentumNetworking.sendToAllTracking(entity, payload);
    }

    public void localPlay(int startTick, int duration) {
        this.tick = startTick;
        this.duration = duration;
    }

    public void localStop() {
        this.tick = 0;
        this.duration = 0;
    }

    void tick() {
        if (duration > 0) tick++;
        if (tick >= duration) localStop();
    }

    public boolean isPlaying() {
        return duration > 0 && tick < duration;
    }

    public boolean justFinished() {
        return isPlaying() && tick == duration - 1;
    }

    public float getProgress() {
        return getProgress(0f);
    }

    public float getProgress(float partialTicks) {
        return duration == 0 ? 0f : ((float) tick + partialTicks) / (float) duration;
    }

    public boolean playSound(
            Entity entity, int atTick,
            SoundEvent sound, SoundSource source,
            float volume, float pitch
    ) {
        if (tick != atTick) return false;
        entity.level().playSound(null, entity, sound, source, volume, pitch);
        return true;
    }
}