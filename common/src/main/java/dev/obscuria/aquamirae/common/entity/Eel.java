package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.common.entity.animation.AnimController;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import dev.obscuria.aquamirae.registry.tags.AquamiraeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Eel extends Monster implements IAnimatable {

	private static final EntityDataAccessor<Integer> MOVE_COOLDOWN = SynchedEntityData.defineId(Eel.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> POS_X = SynchedEntityData.defineId(Eel.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> POS_Y = SynchedEntityData.defineId(Eel.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> POS_Z = SynchedEntityData.defineId(Eel.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> HIT_SERIES = SynchedEntityData.defineId(Eel.class, EntityDataSerializers.INT);

	public final AnimController controller;
	public final AnimState specialIdleState;
	public final AnimState attackState;
	public final AnimState roarState;
	public final AnimState digState;
	public final AnimState deathState;

	private int rareIdle;

	public Eel(EntityType<Eel> type, Level world) {
		super(type, world);
		this.specialIdleState = new AnimState("special_idle", 150);
		this.attackState = new AnimState("attack", 30);
		this.roarState = new AnimState("roar", 100);
		this.digState = new AnimState("dig", 100);
		this.deathState = new AnimState("death", 60);
		this.controller = new AnimController(this,
				specialIdleState, attackState,
				roarState, digState, deathState);
		this.xpReward = 120;
		setPersistenceRequired();
	}

	@Override
	public AnimController getAnimationController() {
		return controller;
	}

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!super.doHurtTarget(entity)) return false;
        AquamiraeHelper.doPoison(this, entity, 100, 2);
        return true;
    }

	@Override
	public AABB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(2f);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(MOVE_COOLDOWN, 200);
		builder.define(POS_X, (float) this.getX());
		builder.define(POS_Y, (float) this.getY());
		builder.define(POS_Z, (float) this.getZ());
		builder.define(HIT_SERIES, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		CompoundTag data = new CompoundTag();
		data.putInt("MoveCooldown", this.getEntityData().get(MOVE_COOLDOWN));
		data.putInt("Hits", this.getEntityData().get(HIT_SERIES));
		data.putFloat("x", this.getEntityData().get(POS_X));
		data.putFloat("y", this.getEntityData().get(POS_Y));
		data.putFloat("z", this.getEntityData().get(POS_Z));
		tag.put("EelData", data);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		CompoundTag data = (CompoundTag) tag.get("EelData");
		if (data == null) return;
		this.getEntityData().set(MOVE_COOLDOWN, data.getInt("MoveCooldown"));
		this.getEntityData().set(HIT_SERIES, data.getInt("Hits"));
		this.getEntityData().set(POS_X, data.getFloat("x"));
		this.getEntityData().set(POS_Y, data.getFloat("y"));
		this.getEntityData().set(POS_Z, data.getFloat("z"));
	}

	@Override
	public void tick() {
		super.tick();
		this.controller.tick();
	}

	// TODO: Move AI logic to the Goals
	@Override
	public void baseTick() {

		attackState.playSound(this, 1, AquamiraeSounds.ENTITY_GENERIC_DEEP_BITE, SoundSource.HOSTILE, 2, 1);
		roarState.playSound(this, 1, AquamiraeSounds.ENTITY_EEL_ROAR, SoundSource.HOSTILE, 2, 0.8f + random.nextFloat() * 0.2f);
		this.rareIdle--;
		final int moveCooldown = this.getEntityData().get(MOVE_COOLDOWN);
		final int hitSeries = this.getEntityData().get(HIT_SERIES);
		this.getEntityData().set(MOVE_COOLDOWN, moveCooldown - 1);

		if (this.rareIdle <= 0 && !this.specialIdleState.isPlaying() && random.nextInt(200) == 1) {
			this.specialIdleState.play(this, random.nextInt(150, 300));
			this.rareIdle = 400;
		}

		if (this.digState.isPlaying()) {
			if (this.digState.getTick() == 25) this.teleportTo(this.getEntityData().get(POS_X), this.getEntityData().get(POS_Y), this.getEntityData().get(POS_Z));
			for (int i = 0; i < 5; i++) this.spawnBlockParticles();

		} else {
			if (this.getTarget() != null) {
				final LivingEntity target = this.getTarget();
				final double distance = this.distanceToSqr(target);
				this.lookControl.setLookAt(target);
				//ATTACK
				if (this.getEntityData().get(HIT_SERIES) <= 0 && distance <= 30 && random.nextInt(40) == 1) {
					this.getEntityData().set(HIT_SERIES, random.nextInt(1, 5));
				} else if (this.getEntityData().get(HIT_SERIES) > 0 && !this.attackState.isPlaying() && !this.roarState.isPlaying()) {
					this.getEntityData().set(HIT_SERIES, hitSeries - 1);
					this.attackState.play(this, 30);
				}
				if (!this.attackState.isPlaying() && !this.roarState.isPlaying() && random.nextInt(100) == 1)
					this.roarState.play(this, 100);
				if (distance <= 24 && this.attackState.getTick() == 14) {
					final float hp = target.getHealth();
					this.doHurtTarget(target);
					if (target.getHealth() < hp) this.heal((hp - target.getHealth()) * 2);
				}
				//ROAR
				if (!target.level().isClientSide() && this.roarState.isPlaying() && this.roarState.getTick() > 12 && this.roarState.getTick() < 52) {
					if (this.tickCount % 5 == 0) {
//						final Vec3 pos = EntityUtils.getRelativePos(this, 4f, 0f, 0f);
//						FlatVFX.Builder.create(20).owner(this).texture(Aquamirae.MOD_ID, "roar")
//								.pos(pos.add(0, this.getEyeY() - pos.y - 0.2, 0))
//								.relativeRot(this, true, true)
//								.xRot(90f, 0f, 0f)
//								.zRot((float) (360 * Math.random()), 0, 0)
//								.moveForward(0f, 3f, 0.03f)
//								.scale(0.4f, 0.1f, 0.01f)
//								.alpha(0.25f, -0.0025f, -0.0025f)
//								.build(this.level());
					}
					if (distance < 150) {
						final Vec3 thisVec = new Vec3(this.getX(), this.getY(), this.getZ());
						final Vec3 targetVec = new Vec3(target.getX(), target.getY(), target.getZ());
						final Vec3 targetDelta = target.getDeltaMovement();
						final Vec3 roarVec = thisVec.vectorTo(targetVec).scale(0.07).scale(Math.max(0, 1.0 - distance / 150.0));
						target.setDeltaMovement(targetDelta.x + roarVec.x, targetDelta.y, targetDelta.z + roarVec.z);
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
                        target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 1));
                        target.setTicksFrozen(target.getTicksFrozen() + 1);
                        if (target instanceof Player player) player.hurtMarked = true;
					}
					if (target instanceof Mob mob) mob.setTarget(this);
				}
			}
			//MOVE
			if (!this.attackState.isPlaying() && !this.roarState.isPlaying() && moveCooldown < 0) {
				final Vec3 pos = this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 40 ? this.targetEelMove() : this.randomEelMove();
				if (pos.y() == 0) {
					this.getEntityData().set(MOVE_COOLDOWN, 2);
				} else {
					this.getEntityData().set(POS_X, (float) pos.x());
					this.getEntityData().set(POS_Y, (float) pos.y());
					this.getEntityData().set(POS_Z, (float) pos.z());
					this.getEntityData().set(MOVE_COOLDOWN, 400);
					this.digState.play(this, 100);
				}
			}
		}
		super.baseTick();
	}

	public void spawnBlockParticles() {
		final BlockState state = this.level().getBlockState(this.blockPosition().below());
		if (state.getRenderShape() != RenderShape.INVISIBLE) {
			this.level().addParticle(
					new BlockParticleOption(ParticleTypes.BLOCK, state),
					this.position().x + (this.random.nextDouble() - 0.5D) * 1.6D,
					this.position().y + 0.03D,
					this.position().z + (this.random.nextDouble() - 0.5D) * 1.6D,
					0, 1.5D, 0);
		}
	}

	public Vec3 randomEelMove() {
		final RandomSource random = RandomSource.create();
		final int x = this.getBlockX() + random.nextInt(5, 16) * (Math.random() > 0.5 ? -1 : 1);
		final int z = this.getBlockZ() + random.nextInt(5, 16) * (Math.random() > 0.5 ? -1 : 1);
		for (int i = -8; i < 8; i++)
			if (checkGround(x, this.getBlockY() - i, z) && checkSpace(x, this.getBlockY() - i + 1, z) && checkSpace(x, this.getBlockY() - i + 2, z)
					&& checkSpace(x, this.getBlockY() - i + 3, z) && checkSpace(x, this.getBlockY() - i + 4, z))
				return new Vec3(x + 0.5, this.getBlockY() - i + 1.0, z + 0.5);
		return Vec3.ZERO;
	}

	public Vec3 targetEelMove() {
		if (this.getTarget() == null) return Vec3.ZERO;
		final RandomSource random = RandomSource.create();
		final int x = this.getTarget().getBlockX() + random.nextInt(-3, 3);
		final int z = this.getTarget().getBlockZ() + random.nextInt(-3, 3);
		for (int i = -4; i < 4; i++)
			if (checkGround(x, this.getBlockY() - i, z) && checkSpace(x, this.getBlockY() - i + 1, z) && checkSpace(x, this.getBlockY() - i + 2, z)
					&& checkSpace(x, this.getBlockY() - i + 3, z) && checkSpace(x, this.getBlockY() - i + 4, z))
				return new Vec3(x + 0.5, this.getBlockY() - i + 1.0, z + 0.5);
		return Vec3.ZERO;
	}

	public boolean checkGround(int x, int y, int z) {
		var pos = new BlockPos(x, y, z);
		return level().getBlockState(pos).is(AquamiraeBlockTags.EEL_DIGGABLE_ON)
				&& level().getBlockState(pos.relative(Direction.NORTH)).is(AquamiraeBlockTags.EEL_DIGGABLE_ON)
				&& level().getBlockState(pos.relative(Direction.SOUTH)).is(AquamiraeBlockTags.EEL_DIGGABLE_ON)
				&& level().getBlockState(pos.relative(Direction.WEST)).is(AquamiraeBlockTags.EEL_DIGGABLE_ON)
				&& level().getBlockState(pos.relative(Direction.EAST)).is(AquamiraeBlockTags.EEL_DIGGABLE_ON);
	}

	public boolean checkSpace(int x, int y, int z) {
		for (var xo = -1; xo <= 1; xo++)
			for (var zo = -1; zo <= 1; zo++)
				if (!level().isEmptyBlock(new BlockPos(x + xo, y, z + zo)))
					return false;
		return true;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public void push(double x, double y, double z) {}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return AquamiraeSounds.ENTITY_GENERIC_DEEP_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return AquamiraeSounds.ENTITY_GENERIC_DEEP_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return AquamiraeSounds.ENTITY_GENERIC_DEEP_DEATH;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.digState.isPlaying()) return false;
		if (source.getDirectEntity() instanceof AbstractArrow) return false;
		if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN)) return false;
		return super.hurt(source, amount);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(
			ServerLevelAccessor level, DifficultyInstance difficulty,
			MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
	) {
		ConfiguredAttribute.applyAttributes(this, CommonConfig.EEL_ATTRIBUTES);
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	@Override
	protected void tickDeath() {
		this.controller.tickDeath(deathState);
	}
}
