package dev.obscuria.aquamirae.common.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractShard extends Projectile {

    protected static final EntityDataAccessor<Float> SCALE;

    public float localXRot;
    public float localXRotO;
    public float localYRot;
    public float localYRotO;

    public AbstractShard(EntityType<? extends AbstractShard> type, Level level) {
        super(type, level);
    }

    public void setShardScale(float value) {
        this.entityData.set(SCALE, value);
    }

    public float getShardScale() {
        return this.entityData.get(SCALE);
    }

    @Override
    public void tick() {
        super.tick();
        final var hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, this.getClipType());
        if (hitResult.getType() != HitResult.Type.MISS) this.onHit(hitResult);
        this.checkInsideBlocks();

        var motion = this.getDeltaMovement();
        final var x = this.getX() + motion.x;
        final var y = this.getY() + motion.y;
        final var z = this.getZ() + motion.z;
        this.rotateToMotion();

        float inertia;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i)
                this.level().addParticle(ParticleTypes.BUBBLE,
                        x - motion.x * 0.25D,
                        y - motion.y * 0.25D,
                        z - motion.z * 0.25D,
                        motion.x, motion.y, motion.z);
            inertia = this.getLiquidInertia();
        } else inertia = this.getInertia();

        this.setDeltaMovement(this.modifyMotion(motion).scale(inertia));
        this.setPos(x, y, z);
        this.makeParticles();

        if (this.tickCount > this.getMaxLifetime() && !this.isRemoved()) this.doBreak();
        this.tickShard();
    }

    protected abstract void tickShard();

    protected abstract void makeParticles();

    protected abstract void onBreak();

    protected abstract float getDamage(Entity target);

    protected abstract void doPostHurtEffects(EntityHitResult hitResult);

    protected ClipContext.Block getClipType() {
        return ClipContext.Block.COLLIDER;
    }

    protected int getMaxLifetime() {
        return 200;
    }

    protected Vec3 modifyMotion(Vec3 motion) {
        return motion;
    }

    protected float getInertia() {
        return 1F;
    }

    protected float getLiquidInertia() {
        return 0.95F;
    }

    protected final void doBreak() {
        this.onBreak();
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        final var entity = result.getEntity();
        final var owner = this.getOwner() instanceof LivingEntity living ? living : null;
        if (entity.hurt(this.damageSources().mobProjectile(this, owner), this.getDamage(entity)))
            this.doPostHurtEffects(result);
        this.doBreak();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.doBreak();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 1f);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Scale", this.entityData.get(SCALE));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(SCALE, tag.getFloat("Scale"));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void rotateToMotion() {
        this.localXRotO = this.localXRot;
        this.localYRotO = this.localYRot;
        final var motion = this.getDeltaMovement();
        final var weight = 0.75f;
        if (motion.lengthSqr() != 0.0D) {
            final var distance = motion.horizontalDistance();
            this.localYRot = (float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F;
            this.localXRot = (float) (Mth.atan2(distance, motion.y) * (double) (180F / (float) Math.PI)) - 90.0F;
            while (this.localXRot - this.localXRotO < -180.0F) this.localXRotO -= 360.0F;
            while (this.localXRot - this.localXRotO >= 180.0F) this.localXRotO += 360.0F;
            while (this.localYRot - this.localYRotO < -180.0F) this.localYRotO -= 360.0F;
            while (this.localYRot - this.localYRotO >= 180.0F) this.localYRotO += 360.0F;
            this.localXRot = Mth.lerp(weight, this.localXRotO, this.localXRot);
            this.localYRot = Mth.lerp(weight, this.localYRotO, this.localYRot);
            if (this.localXRotO == 0) this.localXRotO = this.localXRot;
            if (this.localYRotO == 0) this.localYRotO = this.localYRot;
        }
    }

    static {
        SCALE = SynchedEntityData.defineId(AbstractShard.class, EntityDataSerializers.FLOAT);
    }
}