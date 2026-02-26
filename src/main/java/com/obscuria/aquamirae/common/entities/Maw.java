
package com.obscuria.aquamirae.common.entities;

import com.obscuria.aquamirae.Aquamirae;
import com.obscuria.aquamirae.AquamiraeConfig;
import com.obscuria.aquamirae.registry.AquamiraeEntities;
import com.obscuria.aquamirae.registry.AquamiraeSounds;
import com.obscuria.obscureapi.api.hekate.Animation;
import com.obscuria.obscureapi.api.hekate.AnimationHelper;
import com.obscuria.obscureapi.api.hekate.IAnimated;
import com.obscuria.obscureapi.api.utils.ExceptionFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

@ShipGraveyardEntity
public class Maw extends Monster implements IAnimated {
	private static final EntityDataAccessor<ItemStack> ITEM_IN_MOUTH = SynchedEntityData.defineId(Maw.class, EntityDataSerializers.ITEM_STACK);
	public final Animation ATTACK = new Animation(1);
	public final Animation DEATH = new Animation(2);

	public Maw(PlayMessages.SpawnEntity packet, Level world) {
		this(AquamiraeEntities.MAW.get(), world);
	}

	public Maw(EntityType<Maw> type, Level world) {
		super(type, world);
		xpReward = 10;
		this.randomMawItem();
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(ITEM_IN_MOUTH, ItemStack.EMPTY);
		super.defineSynchedData();
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag data) {
		data.put("ItemInMouth", this.getItemInMouth().save(new CompoundTag()));
		super.addAdditionalSaveData(data);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag data) {
		if (data.contains("ItemInMouth")) this.setItemInMouth(ItemStack.of(data.getCompound("ItemInMouth")));
		super.readAdditionalSaveData(data);
	}

	public void randomMawItem() {
		if (this.level().isClientSide) return;
		final MinecraftServer minecraftServer = this.level().getServer();
		if (minecraftServer != null && this.level() instanceof ServerLevel server) {
			LootParams lootContext = new LootParams.Builder(server)
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.withParameter(LootContextParams.ORIGIN, this.position())
					.create(LootContextParamSets.GIFT);
			LootTable treasure = minecraftServer.getLootData().getLootTable(new ResourceLocation(Aquamirae.MODID, "entities/maw_random_item"));
			ExceptionFilter.of(() -> this.setItemInMouth(treasure.getRandomItems(lootContext).get(0)));
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, (float) 0.4));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false) {
			@Override protected double getAttackReachSqr(@NotNull LivingEntity entity) {
				return 4.0 + entity.getBbWidth() * entity.getBbWidth();
			}
		});
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false, false));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
		this.goalSelector.addGoal(7, new FloatGoal(this));
	}

	public Optional<Animation> getAnimation(byte id) {
		return id == 1 ? Optional.of(ATTACK) : id == 2 ? Optional.of(DEATH) : Optional.empty();
	}

	@Override
	public void tick() {
		AnimationHelper.handleDeath(this, DEATH, 40);
		AnimationHelper.handle(ATTACK, DEATH);
		super.tick();
	}

	public void setItemInMouth(ItemStack stack) {
		this.getEntityData().set(ITEM_IN_MOUTH, stack);
	}

	public ItemStack getItemInMouth() {
		return this.getEntityData().get(ITEM_IN_MOUTH);
	}

	@Override
	public @NotNull MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return AquamiraeSounds.ENTITY_DEEP_AMBIENT.get();
	}

	@Override
	public void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
		this.playSound(SoundEvents.GUARDIAN_FLOP, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(@NotNull DamageSource source) {
		return AquamiraeSounds.ENTITY_DEEP_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return AquamiraeSounds.ENTITY_DEEP_DEATH.get();
	}

	@Override
	protected void dropEquipment() {
		if (this.level() instanceof ServerLevel server && !this.getItemInMouth().isEmpty()) {
			final ItemEntity item = new ItemEntity(EntityType.ITEM, server);
			item.setItem(this.getItemInMouth());
			item.moveTo(this.position());
			item.setDeltaMovement(this.getDeltaMovement().scale(0.25).add(0, 0.2, 0));
			item.hurtMarked = true;
			server.addFreshEntity(item);
			this.setItemInMouth(ItemStack.EMPTY);
		}
	}

	@Override
	public boolean hurt(@NotNull DamageSource source, float amount) {
		if (source.is(DamageTypes.DROWN)) return false;
		return super.hurt(source, amount);
	}

	@Override
	public boolean doHurtTarget(@NotNull Entity entity) {
		this.ATTACK.play(this, 20);
		return super.doHurtTarget(entity);
	}

	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason,
												  @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		Aquamirae.loadFromConfig(this, ForgeMod.SWIM_SPEED.get(), AquamiraeConfig.Common.mawSwimSpeed.get());
		Aquamirae.loadFromConfig(this, Attributes.MOVEMENT_SPEED, AquamiraeConfig.Common.mawSpeed.get());
		Aquamirae.loadFromConfig(this, Attributes.MAX_HEALTH, AquamiraeConfig.Common.mawMaxHealth.get());
		Aquamirae.loadFromConfig(this, Attributes.ARMOR, AquamiraeConfig.Common.mawArmor.get());
		Aquamirae.loadFromConfig(this, Attributes.ATTACK_DAMAGE, AquamiraeConfig.Common.mawAttackDamage.get());
		Aquamirae.loadFromConfig(this, Attributes.FOLLOW_RANGE, AquamiraeConfig.Common.mawFollowRange.get());
		Aquamirae.loadFromConfig(this, Attributes.ATTACK_KNOCKBACK, AquamiraeConfig.Common.mawAttackKnockback.get());
		Aquamirae.loadFromConfig(this, Attributes.KNOCKBACK_RESISTANCE, AquamiraeConfig.Common.mawKnockbackResistance.get());
		return super.finalizeSpawn(world, difficulty, reason, data, tag);
	}

	public static SpawnPlacements.SpawnPredicate<Maw> getSpawnRules() {
		return (entityType, level, spawnType, pos, random) -> pos.getY() < level.getSeaLevel() + 4 &&
				Monster.checkAnyLightMonsterSpawnRules(entityType, level, spawnType, pos, random);
	}

	public static AttributeSupplier.@NotNull Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(ForgeMod.SWIM_SPEED.get(), AquamiraeConfig.DEFAULT_MAW_SWIM_SPEED)
				.add(Attributes.MOVEMENT_SPEED, AquamiraeConfig.DEFAULT_MAW_MOVEMENT_SPEED)
				.add(Attributes.MAX_HEALTH, AquamiraeConfig.DEFAULT_MAW_MAX_HEALTH)
				.add(Attributes.ARMOR, AquamiraeConfig.DEFAULT_MAW_ARMOR)
				.add(Attributes.ATTACK_DAMAGE, AquamiraeConfig.DEFAULT_MAW_ATTACK_DAMAGE)
				.add(Attributes.FOLLOW_RANGE, AquamiraeConfig.DEFAULT_MAW_FOLLOW_RANGE)
				.add(Attributes.ATTACK_KNOCKBACK, AquamiraeConfig.DEFAULT_MAW_ATTACK_KNOCKBACK)
				.add(Attributes.KNOCKBACK_RESISTANCE, AquamiraeConfig.DEFAULT_MAW_KNOCKBACK_RESISTANCE);
	}
}
