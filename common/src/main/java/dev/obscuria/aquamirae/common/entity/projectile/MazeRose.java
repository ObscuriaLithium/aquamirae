package dev.obscuria.aquamirae.common.entity.projectile;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MazeRose extends AbstractChakram {

    private int homingTick;
    private int homingCooldown;

    public MazeRose(EntityType<? extends MazeRose> type, Level level) {
        super(type, level, 40);
    }

    @Override
    protected void onHurtEntity(EntityHitResult result) {
        playSound(SoundEvents.AMETHYST_BLOCK_BREAK);
        if (!(result.getEntity() instanceof LivingEntity entity)) return;
        entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 10));
    }

    @Override
    protected Vec3 computeHomingMovement(Vec3 movement, Vec3 toOwner, double distance) {
        return computeMonsterHoming(super.computeHomingMovement(movement, toOwner, distance));
    }

    private Vec3 computeMonsterHoming(Vec3 movement) {

        this.homingCooldown--;
        if (homingCooldown > 0) return movement;

        @Nullable Monster bestMatch = null;
        double bestDistance = Double.MAX_VALUE;

        for (var entity : level().getEntitiesOfClass(Monster.class, getBoundingBox().inflate(10f))) {
            var distance = entity.distanceToSqr(position());
            if (distance > bestDistance) continue;
            bestDistance = distance;
            bestMatch = entity;
        }

        if (bestMatch == null) {
            this.homingTick = 0;
            return movement;
        } else {
            this.homingTick++;
            var targetCenter = bestMatch.position().lerp(bestMatch.getEyePosition(), 0.5f);
            var toTarget = position().vectorTo(targetCenter).normalize();
            var durationFactor = Mth.clampedLerp(1f, 0f, (float) tickCount / homingDuration);
            var delta = homingTick * 0.2f * durationFactor;
            if (bestDistance < 4f) {
                this.homingCooldown = 10;
                this.homingTick = 0;
            }
            return movement.lerp(toTarget, delta);
        }
    }
}
