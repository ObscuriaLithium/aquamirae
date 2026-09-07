package dev.obscuria.aquamirae.common.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class AbstractChakram extends Projectile {

    private static final int HOMING_DELAY = 5;
    private static final double DESPAWN_DISTANCE = 1.0D;

    protected final int homingDuration;
    protected @Nullable Vec3 launchMovement;

    public AbstractChakram(EntityType<? extends AbstractChakram> type, Level level, int homingDuration) {
        super(type, level);
        this.homingDuration = homingDuration;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        setNoGravity(true);

        var result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (result.getType() != HitResult.Type.MISS) {
            onHit(result);
        }

        applyMovement(computeMovement());
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (ownedBy(target)) return false;
        if (getOwner() instanceof LivingEntity owner && !owner.hasLineOfSight(target)) return false;
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity().hurt(getOwner() instanceof LivingEntity owner
                ? damageSources().mobProjectile(this, owner)
                : damageSources().generic(), 3f)) {
            onHurtEntity(result);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    protected void onHurtEntity(EntityHitResult result) {}

    protected Vec3 computeMovement() {
        @Nullable var owner = getOwner();
        var movement = getDeltaMovement();
        if (owner == null) return movement;

        var toOwner = owner.position().add(0.0D, owner.getEyeHeight() * 0.5D, 0.0D).subtract(position());
        double distance = toOwner.length();

        if (tickCount > HOMING_DELAY && distance < DESPAWN_DISTANCE) {
            discard();
        }

        if (tickCount > HOMING_DELAY) {
            movement = computeHomingMovement(movement, toOwner, distance);
        }

        return movement;
    }

    protected Vec3 computeHomingMovement(Vec3 movement, Vec3 toOwner, double distance) {
        if (distance <= 1.0E-5D) return movement;

        if (launchMovement == null) {
            launchMovement = movement;
        }

        double launchSpeed = launchMovement.length();
        if (launchSpeed <= 1.0E-5D) return movement;

        var targetMovement = toOwner.scale(launchSpeed / distance);
        double homingFactor = (tickCount - HOMING_DELAY) / (double) homingDuration;
        double turnFactor = Mth.clamp(homingFactor, 0.0D, 1.0D);
        return launchMovement.scale(1.0D - turnFactor).add(targetMovement.scale(turnFactor));
    }

    protected void applyMovement(Vec3 movement) {
        setDeltaMovement(movement);
        setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);
        updateRotation(movement);
    }

    private void updateRotation(Vec3 movement) {
        double horizontalDistance = movement.horizontalDistance();
        float targetYRot = (float) (Mth.atan2(movement.x, movement.z) * (180D / Math.PI));
        float targetXRot = (float) (Mth.atan2(movement.y, horizontalDistance) * (180D / Math.PI));

        this.yRotO = getYRot();
        this.xRotO = getXRot();
        setYRot(lerpRotation(this.yRotO, targetYRot));
        setXRot(lerpRotation(this.xRotO, targetXRot));
    }
}