package dev.obscuria.aquamirae.common.entity.animation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public final class AnimController {

    private final LivingEntity owner;
    private final AnimState[] states;

    public AnimController(LivingEntity owner, AnimState... states) {
        this.owner = owner;
        this.states = states;
    }

    public void tick() {
        for (var state : states) state.tick();
    }

    public boolean tickDeath(AnimState deathAnim) {
        if (!owner.isDeadOrDying()) return false;
        if (!deathAnim.isPlaying()) deathAnim.play(owner);
        if (deathAnim.justFinished() && !owner.isRemoved()) {
            owner.deathTime = 20;
            owner.level().broadcastEntityEvent(owner, (byte) 60);
            owner.remove(Entity.RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    public Optional<AnimState> find(String animationName) {
        for (var state : states) {
            if (!state.getName().equals(animationName)) continue;
            return Optional.of(state);
        }
        return Optional.empty();
    }
}