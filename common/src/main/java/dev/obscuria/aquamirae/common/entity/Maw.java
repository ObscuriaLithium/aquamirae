package dev.obscuria.aquamirae.common.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.entity.animation.AnimController;
import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.config.ConfiguredAttribute;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import javax.annotation.Nullable;

public class Maw extends Monster implements IAnimatable {

    protected static final EntityDataAccessor<ItemStack> DATA_ID_MOUTH_ITEM;
    protected static final ResourceKey<LootTable> TABLE_MOUTH_ITEM;
    protected static final String TAG_MOUTH_ITEM = "MouthItem";
    protected static final double MOUTH_ITEM_CHANCE = 0.1;

    public final AnimState attackState;
    public final AnimState deathState;
    public final AnimController controller;

    public Maw(EntityType<? extends Maw> type, Level level) {
        super(type, level);
        this.attackState = new AnimState("attack", 20);
        this.deathState = new AnimState("death", 40);
        this.controller = new AnimController(this, attackState, deathState);
        this.xpReward = 10;
    }

    public void setMouthItem(ItemStack stack) {
        this.entityData.set(DATA_ID_MOUTH_ITEM, stack);
    }

    public ItemStack getMouthItem() {
        return entityData.get(DATA_ID_MOUTH_ITEM);
    }

    @Override
    public AnimController getAnimationController() {
        return controller;
    }

    @Override
    public void tick() {
        super.tick();
        this.controller.tick();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.attackState.play(this);
        return super.doHurtTarget(entity);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty,
            MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        generateMouthItem(level);
        ConfiguredAttribute.applyAttributes(this, CommonConfig.MAW_ATTRIBUTES);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put(TAG_MOUTH_ITEM, getMouthItem().saveOptional(level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(TAG_MOUTH_ITEM, CompoundTag.TAG_COMPOUND))
            setMouthItem(ItemStack.parseOptional(level().registryAccess(), compound.getCompound(TAG_MOUTH_ITEM)));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_MOUTH_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void tickDeath() {
        this.controller.tickDeath(deathState);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        var mouthItem = getMouthItem();
        if (mouthItem.isEmpty()) return;
        spawnAtLocation(mouthItem);
        setMouthItem(ItemStack.EMPTY);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.GUARDIAN_FLOP, 0.2f, 0.75f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AquamiraeSounds.ENTITY_GENERIC_DEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AquamiraeSounds.ENTITY_GENERIC_DEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AquamiraeSounds.ENTITY_GENERIC_DEEP_DEATH;
    }

    protected void generateMouthItem(ServerLevelAccessor accessor) {
        if (!(accessor instanceof ServerLevel level)) return;
        if (random.nextDouble() > MOUTH_ITEM_CHANCE) return;
        var lootContext = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .create(LootContextParamSets.GIFT);
        var lootTable = level.getServer().reloadableRegistries().getLootTable(TABLE_MOUTH_ITEM);
        var items = lootTable.getRandomItems(lootContext);
        if (items.isEmpty()) return;
        this.setMouthItem(items.getFirst());
    }

    @SuppressWarnings("deprecation")
    public static boolean checkSpawnRules(
            EntityType<Maw> type, ServerLevelAccessor levelAccessor,
            MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return checkAnyLightMonsterSpawnRules(type, levelAccessor, spawnType, pos, random)
                && pos.getY() > levelAccessor.getSeaLevel() - 4
                && pos.getY() < levelAccessor.getSeaLevel() + 4
                && levelAccessor.getBrightness(LightLayer.BLOCK, pos) <= 4;
    }

    static {
        DATA_ID_MOUTH_ITEM = SynchedEntityData.defineId(Maw.class, EntityDataSerializers.ITEM_STACK);
        TABLE_MOUTH_ITEM = ResourceKey.create(Registries.LOOT_TABLE, Aquamirae.id("entities/maw_random_item"));
    }
}
