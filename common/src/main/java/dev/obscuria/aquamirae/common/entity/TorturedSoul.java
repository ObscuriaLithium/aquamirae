package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.common.entity.animation.AnimController;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class TorturedSoul extends Monster implements IAnimatable {

    public final AnimController controller;
    public final AnimState attackState;

    public TorturedSoul(EntityType<TorturedSoul> type, Level world) {
        super(type, world);
        this.attackState = new AnimState("attack", 20);
        this.controller = new AnimController(this, attackState);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2f, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Monster.class, (float) 6));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
    }

    @Override
    public AnimController getAnimationController() {
        return controller;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source.is(DamageTypes.DROWN)) return false;
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.attackState.play(this);
        return super.doHurtTarget(entity);
    }

    @Override
    public void tick() {
        super.tick();
        this.controller.tick();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        ConfiguredAttribute.applyAttributes(this, CommonConfig.TORTURED_SOUL_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    //	@Override
//	public void baseTick() {
//		AnimationHelper.handle(attackState);
//		if (this.getTarget() != null && !this.hasEffect(MobEffects.INVISIBILITY)) {
//			this.getPersistentData().putDouble("charge", this.getPersistentData().getDouble("charge") + 1);
//			if (this.getPersistentData().getDouble("charge") > 200) {
//				this.getPersistentData().putDouble("charge", 0);
//				this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 2, (true), (false)));
//				this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, (true), (false)));
//				if (this.level() instanceof ServerLevel server)
//					server.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1, this.getZ(), 10, 0.4, 0.8, 0.4, 0);
//				this.setDeltaMovement(new Vec3(this.getDeltaMovement().x(), 0.2, this.getDeltaMovement().z()));
//			}
//		}
//		if (this.hasEffect(MobEffects.INVISIBILITY)) {
//			this.getPersistentData().putDouble("flame", this.getPersistentData().getDouble("flame") + 1);
//			this.getPersistentData().putDouble("smoke", this.getPersistentData().getDouble("smoke") + 1);
//			if (this.getPersistentData().getDouble("smoke") > 1) {
//				this.getPersistentData().putDouble("smoke", 0);
//				if (this.level() instanceof ServerLevel server)
//					server.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1.3, this.getZ(), 1, 0.14, 0.2, 0.14, 0);
//			}
//			if (this.getPersistentData().getDouble("flame") > 6) {
//				this.getPersistentData().putDouble("flame", 0);
//				if (this.level() instanceof ServerLevel server)
//					server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY() + 1.3, this.getZ(), 1, 0.1, 0.15, 0.1, 0);
//			}
//		}
//		super.baseTick();
//	}

    public static boolean checkSpawnRules(
            EntityType<TorturedSoul> type, ServerLevelAccessor levelAccessor,
            MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return Monster.checkMonsterSpawnRules(type, levelAccessor, spawnType, pos, random);
    }
}
